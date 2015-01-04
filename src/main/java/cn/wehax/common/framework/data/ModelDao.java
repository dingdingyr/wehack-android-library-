package cn.wehax.common.framework.data;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.inject.Inject;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.wehax.common.framework.data.annotations.Id;
import cn.wehax.common.framework.data.annotations.ObjectFrom;
import cn.wehax.common.framework.data.annotations.RemoteQuery;
import cn.wehax.common.framework.data.annotations.ValueFrom;
import cn.wehax.common.framework.data.helper.ObjectHelper;
import cn.wehax.common.framework.model.ErrorBean;
import cn.wehax.common.framework.model.IBaseBean;
import cn.wehax.common.framework.model.IDataListCallback;
import cn.wehax.common.volley.RequestManager;
import roboguice.RoboGuice;

/**
 * Created by Terry on 14/12/20.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public class ModelDao<T extends IBaseBean> {


    @Inject
    RequestManager requestManager;

    Dao<T, Integer> dao;

    OrmLiteSqliteOpenHelper ormHelper;

    public ModelDao(Class<T> clazz, OrmLiteSqliteOpenHelper ormHelper, Context context) {
        this.ormHelper = ormHelper;
        try {
            dao = ormHelper.getDao(clazz);
            RoboGuice.injectMembers(context, this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void fillList(List<T> data, int strategy, IDataListCallback<T> callback) {
        assert (data.size() > 0);
        Class<?> clazz = data.get(0).getClass();
        switch (strategy) {
            case DataStrategy.CACHE_POLICY_NETWORK_ONLY:
                fillListRemote(data, clazz, callback);
                break;
            case DataStrategy.CACHE_POLICY_CACHE_ONLY:
                for (T dataItem : data) {
                    fillSingleUsingLocal(dataItem);
                }
                callback.onDataListReturn(data, -1, -1);
                break;


        }

    }

    private Map<String, T> listToMap(List<T> data, Class<?> clazz) {
        Map<String, T> map = new HashMap<>();
        final Field idField = ObjectHelper.findFieldWithAnnotation(clazz, Id.class);
        if (idField == null) {
            return map;
        }

        idField.setAccessible(true);

        try {

            for (T dataItem : data) {

                String itemId = (String) idField.get(dataItem);
                map.put(itemId, dataItem);

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }

    private boolean fillSingleUsingLocal(T data) {

        try {
            List<T> tempData = dao.queryForMatching(data);
            if (tempData.size() > 0) {
                ObjectHelper.copy(tempData.get(0), data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void updateLocalWithData(T data) {
        try {
            dao.createOrUpdate(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private String implodeListIds(List<T> data, Class<?> clazz) {
        final Field idField = ObjectHelper.findFieldWithAnnotation(clazz, Id.class);
        idField.setAccessible(true);
        String str = "";
        try {
            for (T dataItem : data) {

                String itemId = (String) idField.get(dataItem);
                str += itemId + ",";

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;


    }

    public void fillListRemote(final List<T> data, Class<?> clazz, final IDataListCallback<T> callback) {

        final Map<String, T> map = listToMap(data, clazz);


        if (!clazz.isAnnotationPresent(RemoteQuery.class)) {
            return;
        }

        RemoteQuery annotation = clazz.getAnnotation(RemoteQuery.class);
        String urlFormat = annotation.url();

        if (urlFormat == null) {
            return;
        }

        String remoteId = implodeListIds(data, clazz);

        String url = urlFormat.replace("{?}", remoteId);


        final Field idField = ObjectHelper.findFieldWithAnnotation(clazz, Id.class);

        if (idField == null) {
            return;
        }

        final String idDataKey = idField.getAnnotation(Id.class).dataKey();

        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject obj = response.getJSONObject(i);
                                String id = obj.getString(idDataKey);
                                T obj2 = map.get(id);
                                if (obj2 == null) continue;
                                idField.set(obj2, id);

                                Field[] fields = obj2.getClass().getFields();

                                for (Field field : fields) {
                                    if (field.isAnnotationPresent(ValueFrom.class)) {
                                        ValueFrom valueAnnotation = field.getAnnotation(ValueFrom.class);
                                        String dataKey = valueAnnotation.dataKey();
                                        ObjectHelper.setField(obj2, field, obj, dataKey);

                                    } else if (field.isAnnotationPresent(ObjectFrom.class)) {
                                        ObjectFrom objectAnnotation = field.getAnnotation(ObjectFrom.class);
                                        String dataKey = objectAnnotation.dataKey();
                                        Object subObj = field.getClass().newInstance();
                                        Field subIdField = ObjectHelper.findFieldWithAnnotation(field.getClass(), Id.class);
                                        ObjectHelper.setField(subObj, subIdField, obj, dataKey);

                                        field.set(obj2, subObj);

                                    }

                                }
                                updateLocalWithData(obj2);

                            }

                            if (callback != null) {
                                callback.onDataListReturn(data, -1, -1);
                            }

                        } catch (JSONException | IllegalAccessException | InstantiationException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) {
                            callback.onError(new ErrorBean(255, error.getMessage()));
                        }

                    }
                }

        );
        requestManager.getRequestQueue().add(arrayRequest);


        //输入一个除了id和updateTime为空的bean列表
        //遍历所有bean的id属性，并发送给服务器。
        //从服务器返回的数据列表中获得JsonArray
        //遍历Array中每个JsonObject，对每个标有@ObjectFrom的属性
        // 都创建一个对应类型的对象，对其他属性则直接填充bean的对应属性。
    }


}
