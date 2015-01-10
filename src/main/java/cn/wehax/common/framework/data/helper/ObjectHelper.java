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

/**
 * Created by Terry on 15/1/4.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public class ObjectHelper {

    /**
     * 将JSONObject里的一个value填充到对象的某个Field里。
     *
     * @param obj        需填充的对象
     * @param field      需填充的Field
     * @param srcJsonObj 来源数据
     * @param dataKey    数据key
     */
    public static void setFieldUsingJson(Object obj, Field field, JSONObject srcJsonObj, String dataKey) {
        try {
            field.setAccessible(true);
            String type = field.getType().toString();
            if (field.getType().equals(String.class)
                    || type.equalsIgnoreCase("String")) {

                field.set(obj, srcJsonObj.optString(dataKey));

            } else if (field.getType().equals(Integer.class)
                    || type.equalsIgnoreCase("int")) {

                field.set(obj, srcJsonObj.optInt(dataKey));

            } else if (field.getType().equals(Double.class)
                    || type.equalsIgnoreCase("double")) {

                field.set(obj, srcJsonObj.optDouble(dataKey));

            } else if (field.getType().equals(Long.class)
                    || type.equalsIgnoreCase("long")) {

                field.set(obj, srcJsonObj.optLong(dataKey));

            } else if (field.getType().equals(Boolean.class)
                    || type.equalsIgnoreCase("boolean")) {

                field.set(obj, srcJsonObj.optBoolean(dataKey));

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * 找出具有某个Annotation的Field。
     *
     * @param targetClazz     需寻找的Class类型。
     * @param annotationClazz 注释类型
     * @param <T>
     * @return 标注了annotationClazz的第一个Field
     */
    public static <T extends Annotation> Field findFieldWithAnnotation(Class<?> targetClazz, Class<T> annotationClazz) {

        final Field[] fields = targetClazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(annotationClazz)) {
                return field;
            }
        }
        return null;

    }

    /**
     * 找出具有某个Annotation的所有Field。
     *
     * @param targetClazz     需寻找的Class类型。
     * @param annotationClazz 注释类型
     * @param <T>
     * @return 标注了annotationClazz的所有Field
     */

    public static <T extends Annotation> List<Field> findFieldListWithAnnotation(Class<?> targetClazz, Class<T> annotationClazz) {

        final Field[] fields = targetClazz.getDeclaredFields();

        List<Field> newFields = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotationClazz)) {
                newFields.add(field);
            }
        }
        return newFields;

    }


    /**
     * 依照标注，将JSON数据解析成某个对象。<br/>
     * 有效标注有 Id,ValueFrom, ObjectFrom. <br/>
     * 其中的的dataKey方法表示将JSON里的哪个字段解析到这个对象。<br/>
     * ObjectFrom的dataKey字段，在JSON数据里应该也是一个object。 <br/>
     *
     * @param jsonObj 数据来源的JSON
     * @param clazz   需解析成的Class类型
     * @param <T>     类型
     * @return 返回一个新的对象。
     * @throws JSONException
     */
    public static <T> T parseJSONToObject(JSONObject jsonObj, Class<T> clazz) throws JSONException {


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
                    if (obj != null) {
                        Field subIdField = findFieldWithAnnotation(field.getType(), Id.class);
                        String subIdDataKey = subIdField.getAnnotation(Id.class).dataKey();
                        String subId = obj.optString(subIdDataKey);
                        if (TextUtils.isEmpty(subId)) {
                            subId = jsonObj.optString(subIdDataKey);
                        }
                        subIdField.setAccessible(true);
                        subIdField.set(subObj, subId);

                        Field[] subFields = subObj.getClass().getDeclaredFields();
                        for (Field subField : subFields) {
                            if (subField.isAnnotationPresent(ValueFrom.class)) {
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
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 参见 parseJSONToObject(JSONObject jsonObj, Class<T> clazz) ,
     * 重载第一个参数为String。
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @throws JSONException
     */
    public static <T> T parseJSONToObject(String json, Class<T> clazz) throws JSONException {
        try {
            JSONObject obj = new JSONObject(json);
            return parseJSONToObject(obj, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将同类型的一个对象赋值到另一个同类型的对象里。
     * @param from
     * @param to
     * @param <T>
     * @return
     */
    public static <T extends IBaseBean> boolean copy(T from, T to) {
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
