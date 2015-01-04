package cn.wehax.common.framework.data.helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import cn.wehax.common.framework.data.annotations.Id;
import cn.wehax.common.framework.data.annotations.ObjectFrom;
import cn.wehax.common.framework.data.annotations.ValueFrom;

/**
 * Created by Terry on 15/1/4.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public class ObjectHelper {

    public static void setField(Object obj2, Field field, JSONObject obj, String dataKey) {
        try {
            if (field.getType().equals(String.class)) {
                field.set(obj2, obj.optString(dataKey));
            } else if (field.getType().equals(Integer.class)) {
                field.set(obj2, obj.optInt(dataKey));
            } else if (field.getType().equals(Double.class)) {
                field.set(obj2, obj.optDouble(dataKey));
            } else if (field.getType().equals(Long.class)) {
                field.set(obj2, obj.optLong(dataKey));
            } else if (field.getType().equals(Boolean.class)) {
                field.set(obj2, obj.optBoolean(dataKey));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public static <T extends Annotation> Field findFieldWithAnnotation(Class<?> targetClazz, Class<T> annoClazz) {

        final Field[] fields = targetClazz.getFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(annoClazz)) {
                return field;
            }
        }
        return null;

    }

    public static <T> T parseJson(JSONObject obj, Class<T> clazz)  {


        try {
            final Field idField = findFieldWithAnnotation(clazz, Id.class);

            if (idField == null) {
                return null;
            }

            final String idDataKey = idField.getAnnotation(Id.class).dataKey();

            String id = obj.getString(idDataKey);
            T classObj = clazz.newInstance();

            idField.set(classObj, id);

            Field[] fields = classObj.getClass().getFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(ValueFrom.class)) {
                    ValueFrom valueAnnotation = field.getAnnotation(ValueFrom.class);
                    String dataKey = valueAnnotation.dataKey();
                    setField(classObj, field, obj, dataKey);

                } else if (field.isAnnotationPresent(ObjectFrom.class)) {
                    ObjectFrom objectAnnotation = field.getAnnotation(ObjectFrom.class);
                    String dataKey = objectAnnotation.dataKey();
                    Object subObj = field.getClass().newInstance();
                    Field subIdField = findFieldWithAnnotation(field.getClass(), Id.class);
                    setField(subObj, subIdField, obj, dataKey);

                    field.set(classObj, subObj);

                }

            }
            return classObj;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T parseJson(String json, Class<T> clazz) {
        try {
            JSONObject obj = new JSONObject(json);
            return parseJson(obj, clazz);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
