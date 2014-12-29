package cn.wehax.common.framework.data;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.wehax.common.framework.data.annotations.Id;
import cn.wehax.common.framework.data.annotations.ObjectFrom;
import cn.wehax.common.framework.data.annotations.RemoteQuery;
import cn.wehax.common.framework.data.annotations.ValueFrom;
import cn.wehax.common.framework.model.ErrorBean;
import cn.wehax.common.framework.model.IBaseBean;
import cn.wehax.common.framework.model.IDataCallback;
import cn.wehax.common.framework.model.IDataListCallback;
import cn.wehax.util.TextUtils;

/**
 * Created by Terry on 14/12/20.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
@Singleton
public class ModelDao {


    private Application mContext;

    @Inject
    RequestManager requestManager;

    public ModelDao(Provider<Application> provider) {
        mContext = provider.get();
    }


    public SQLiteOpenHelper sqLiteOpenHelper;


    public <T extends IBaseBean> void fillList(List<T> data, int strategy,IDataListCallback<T> callback) {
        assert (data.size() > 0);
        Class<?> clazz = data.get(0).getClass();
        switch (strategy) {
            case DataStrategy.CACHE_POLICY_NETWORK_ONLY:
               fillListRemote(data,clazz,callback);
                break;
            case DataStrategy.CACHE_POLICY_CACHE_ONLY:
                for(T dataItem :data){
                    fillSingleUsingLocal(dataItem);
                }
                callback.onDataListReturn(data, -1, -1);
                break;


        }

    }

    private <T extends IBaseBean> Map<String, T> listToMap(List<T> data, Class<?> clazz) {
        Map<String, T> map = new HashMap<>();
        final Field idField = findFieldWithAnnotation(clazz, Id.class);
        idField.setAccessible(true);

        if (idField == null) {
            return map;
        }
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

    private <T extends IBaseBean> boolean fillSingleUsingLocal(T data) {
        SQLiteDatabase db =sqLiteOpenHelper.getWritableDatabase();

        return false;
    }


    private <T extends Annotation> Field findFieldWithAnnotation(Class<?> targetClazz, Class<T> annoClazz) {

        final Field[] fields = targetClazz.getFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(annoClazz)) {
                return field;
            }
        }
        return null;

    }

    private void setField(Object obj2, Field field, JSONObject obj, String dataKey) {
        try {
            if (field.getType().equals(String.class)) {
                field.set(obj2, obj.getString(dataKey));
            } else if (field.getType().equals(Integer.class)) {
                field.set(obj2, obj.getInt(dataKey));
            } else if (field.getType().equals(Double.class)) {
                field.set(obj2, obj.getDouble(dataKey));
            } else if (field.getType().equals(Long.class)) {
                field.set(obj2, obj.getLong(dataKey));
            }
        } catch (IllegalAccessException | JSONException e) {
            e.printStackTrace();
        }

    }

    private <T extends IBaseBean> String implodeListIds(List<T> data, Class<?> clazz) {
        final Field idField = findFieldWithAnnotation(clazz, Id.class);
        idField.setAccessible(true);
        String str = "";
        try {
            for (T dataItem : data) {

                String itemId = (String) idField.get(dataItem);
                str +=itemId+",";

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(str.length()>0){
            str = str.substring(0,str.length()-1);
        }
        return str;


    }

    public <T extends IBaseBean> void fillListRemote(final List<T> data, Class<?> clazz, final IDataListCallback<T> callback) {

        final Map<String, T> map = listToMap(data, clazz);


        if (!clazz.isAnnotationPresent(RemoteQuery.class)) {
            return;
        }

        RemoteQuery annotation = clazz.getAnnotation(RemoteQuery.class);
        String urlFormat = annotation.url();

        if (urlFormat == null) {
            return;
        }

        String remoteId = implodeListIds(data,clazz);

        String url = urlFormat.replace("{?}", remoteId);


        final Field idField = findFieldWithAnnotation(clazz, Id.class);

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
                                        setField(obj2, field, obj, dataKey);

                                    } else if (field.isAnnotationPresent(ObjectFrom.class)) {
                                        ObjectFrom objectAnnotation = field.getAnnotation(ObjectFrom.class);
                                        String dataKey = objectAnnotation.dataKey();
                                        Object subObj = field.getClass().newInstance();
                                        Field subIdField = findFieldWithAnnotation(field.getClass(), Id.class);
                                        setField(subObj, subIdField, obj, dataKey);

                                        field.set(obj2, subObj);

                                    }

                                }

                            }

                            if(callback != null){
                                callback.onDataListReturn(data,-1,-1);
                            }

                        } catch (JSONException | IllegalAccessException | InstantiationException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(callback!= null){
                            callback.onError(new ErrorBean(255,error.getMessage()));
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
