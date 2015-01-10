package cn.wehax.common.framework.data.helper;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.wehax.common.framework.data.annotations.Id;
import cn.wehax.common.framework.data.annotations.ObjectFrom;
import cn.wehax.common.framework.data.annotations.ValueFrom;
import cn.wehax.common.framework.model.IBaseBean;
import roboguice.util.Ln;

/**
 * Created by Terry on 15/1/4.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public class ObjectHelper {

    public static void setFieldUsingJson(Object obj, Field field, JSONObject jsonObject, String dataKey) {
        try {
            field.setAccessible(true);
            String type = field.getType().toString();
            if (field.getType().equals(String.class)
                    || type.equalsIgnoreCase("String")) {

                field.set(obj, jsonObject.optString(dataKey));

            } else if (field.getType().equals(Integer.class)
                    || type.equalsIgnoreCase("int")) {

                field.set(obj, jsonObject.optInt(dataKey));

            } else if (field.getType().equals(Double.class)
                    || type.equalsIgnoreCase("double")) {

                field.set(obj, jsonObject.optDouble(dataKey));

            } else if (field.getType().equals(Long.class)
                    || type.equalsIgnoreCase("long")) {

                field.set(obj, jsonObject.optLong(dataKey));

            } else if (field.getType().equals(Boolean.class)
                    || type.equalsIgnoreCase("boolean")) {

                field.set(obj, jsonObject.optBoolean(dataKey));

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public static <T extends Annotation> Field findFieldWithAnnotation(Class<?> targetClazz, Class<T> annoClazz) {

        final Field[] fields = targetClazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(annoClazz)) {
                return field;
            }
        }
        return null;

    }

    public static <T extends Annotation> List<Field> findFieldListWithAnnotation(Class<?> targetClazz, Class<T> annoClazz) {

        final Field[] fields = targetClazz.getDeclaredFields();

        List<Field> newFields = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annoClazz)) {
                newFields.add(field);
            }
        }
        return newFields;

    }


    public static <T> T parseJsonToObject(JSONObject jsonObj, Class<T> clazz) throws JSONException {


        try {
            final Field idField = findFieldWithAnnotation(clazz, Id.class);

            if (idField == null) {
                return null;
            }

            final String idDataKey = idField.getAnnotation(Id.class).dataKey();

            String id = jsonObj.getString(idDataKey);
            T classObj = clazz.newInstance();
            idField.setAccessible(true);
            idField.set(classObj, id);

            Field[] fields = classObj.getClass().getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(ValueFrom.class)) {
                    ValueFrom valueAnnotation = field.getAnnotation(ValueFrom.class);
                    String dataKey = valueAnnotation.dataKey();
                    setFieldUsingJson(classObj, field, jsonObj, dataKey);


                } else if (field.isAnnotationPresent(ObjectFrom.class)) {
                    ObjectFrom objectAnnotation = field.getAnnotation(ObjectFrom.class);
                    String dataKey = objectAnnotation.dataKey();
                    Object subObj = field.getType().newInstance();
                    JSONObject obj = jsonObj.optJSONObject(dataKey);
                    if(obj != null){
                        Field subIdField = findFieldWithAnnotation(field.getType(), Id.class);
                        String subIdDataKey = subIdField.getAnnotation(Id.class).dataKey();
                        String subId = obj.optString(subIdDataKey);
                        if(TextUtils.isEmpty(subId)){
                            subId = jsonObj.optString(subIdDataKey);
                        }
                        subIdField.setAccessible(true);
                        subIdField.set(subObj, subId);

                        Field[] subFields = subObj.getClass().getDeclaredFields();
                        for(Field subField:subFields){
                            if(subField.isAnnotationPresent(ValueFrom.class)){
                                ValueFrom valueFrom = subField.getAnnotation(ValueFrom.class);
                                String subDataKey = valueFrom.dataKey();
                                setFieldUsingJson(subObj, subField, obj, subDataKey);
                            }
                        }
                    }


                    field.setAccessible(true);
                    field.set(classObj, subObj);

                }

            }
            return classObj;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T parseJsonToObject(String json, Class<T> clazz) throws JSONException {
        try {
            JSONObject obj = new JSONObject(json);
            return parseJsonToObject(obj, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T extends IBaseBean> boolean copy(T from, T to) {
        //TODO: copy every field
        final Field[] fields = from.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(to, field.get(from));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;


    }
}
