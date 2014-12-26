package cn.wehax.common.framework.data;

import android.app.Application;

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
import java.util.List;
import java.util.Map;

import cn.wehax.common.framework.data.annotations.Id;
import cn.wehax.common.framework.data.annotations.ObjectFrom;
import cn.wehax.common.framework.data.annotations.RemoteQuery;
import cn.wehax.common.framework.data.annotations.ValueFrom;
import cn.wehax.common.framework.model.IBaseBean;
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


    public <T extends IBaseBean> void fillList(List<T> data, int strategy) {

    }

    private <T extends IBaseBean> boolean fillSingleUsingLocal(T data) {
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

    /**
     * @param map
     * @param idList
     * @param <T>
     */
    public <T extends IBaseBean> void fillListRemote(final Map<String, T> map, String... idList) {

        if (idList.length <= 0) {
            return;
        }

        T sampleObj = map.get(idList[0]);
        if (sampleObj == null) {
            return;
        }
        Class<?> clazz = sampleObj.getClass();

        if (!clazz.isAnnotationPresent(RemoteQuery.class)) {
            return;
        }

        RemoteQuery annotation = clazz.getAnnotation(RemoteQuery.class);
        String urlFormat = annotation.url();

        if (urlFormat == null) {
            return;
        }

        String remoteId = TextUtils.implode(",", idList);

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
                        } catch (JSONException | IllegalAccessException | InstantiationException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

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
