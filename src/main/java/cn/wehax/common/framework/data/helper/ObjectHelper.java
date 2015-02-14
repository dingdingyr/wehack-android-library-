package cn.wehax.common.framework.data.helper;

import android.text.TextUtils;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.wehax.common.framework.data.annotations.Id;
import cn.wehax.common.framework.data.annotations.ObjectFrom;
import cn.wehax.common.framework.data.annotations.ValueFrom;
import cn.wehax.common.framework.model.IBaseBean;
import cn.wehax.common.framework.model.IDataBean;

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

                String str= srcJsonObj.optString(dataKey);
                if(str.equals("null") || str.length()<=0){
                    return;
                }
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

        final List<Field> fields = getAllFields(targetClazz);

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

        final List<Field> fields = getAllFields(targetClazz);

        List<Field> newFields = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotationClazz)) {
                newFields.add(field);
            }
        }
        return newFields;

    }

    public static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
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
    public static <T extends IDataBean> T parseJSONToObject(JSONObject jsonObj, Class<T> clazz) throws JSONException {


        try {

            T classObj = clazz.newInstance();
            classObj.setComplete(true);


            final Field idField = findFieldWithAnnotation(clazz, Id.class);
            if (idField != null) {
                final String idDataKey = idField.getAnnotation(Id.class).dataKey();
                String id = jsonObj.optString(idDataKey);
                idField.setAccessible(true);
                idField.set(classObj, id);
            }


            final List<Field> fields = getAllFields(classObj.getClass());


            for (Field field : fields) {
                if (field.isAnnotationPresent(ValueFrom.class)) {
                    ValueFrom valueAnnotation = field.getAnnotation(ValueFrom.class);
                    String dataKey = valueAnnotation.dataKey();
                    setFieldUsingJson(classObj, field, jsonObj, dataKey);


                } else if (field.isAnnotationPresent(ObjectFrom.class)) {
                    ObjectFrom objectAnnotation = field.getAnnotation(ObjectFrom.class);
                    String dataKey = objectAnnotation.dataKey();
                    Object subObj = field.getType().newInstance();
                    if (subObj instanceof IDataBean) {
                        IDataBean bean = (IDataBean) subObj;
                        bean.setComplete(false);
                    }
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
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @throws JSONException
     */
    public static <T extends IDataBean> T parseJSONToObject(String json, Class<T> clazz) throws JSONException {
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
     *
     * @param from
     * @param to
     * @param <T>
     * @return
     */
    public static <T extends IBaseBean> boolean copy(T from, T to) {
        assert (to.getClass().isAssignableFrom(from.getClass()));
        List<Field> fields = getAllFields(to.getClass());
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

    public static <T extends IBaseBean> boolean merge(T from, T to) {
        List<Field> fields = getAllFields(from.getClass());
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object obj = field.get(from);
                if (hasChangedValue(obj)) {
                    field.set(to, field.get(from));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;


    }

    public static boolean hasChangedValue(Object obj) {
        if (obj != null) {
            Class<?> objClass = obj.getClass();
            String type = objClass.toString();
            if (objClass.equals(String.class)
                    || type.equalsIgnoreCase("String")) {
                return String.valueOf(obj).length() > 0;

            } else if (objClass.equals(Integer.class)
                    || type.equalsIgnoreCase("int")) {
                return (int) obj != 0;

            } else if (objClass.equals(Double.class)
                    || type.equalsIgnoreCase("double")) {

                return (double) obj != 0;

            } else if (objClass.equals(Long.class)
                    || type.equalsIgnoreCase("long")) {

                return (long) obj != 0;

            } else if (objClass.equals(Boolean.class)
                    || type.equalsIgnoreCase("boolean")) {

                return (boolean) obj;

            }
            return true;
        } else {
            return false;
        }
    }

    public static <T extends IDataBean> boolean persistSelfAndForeignToDB(T data, Dao<T, Object> dao, OrmLiteSqliteOpenHelper ormHelper) {
        try {
            data.setComplete(true);
            dao.createOrUpdate(data);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        List<Field> toBeCreateField = ObjectHelper.findFieldListWithAnnotation(data.getClass(), ObjectFrom.class);
        try {
            for (Field field : toBeCreateField) {
                field.setAccessible(true);
                Object obj = field.get(data);
                if (obj != null) {

                } else {
                    continue;
                }
                Class<?> clazz = field.getType();
                if (IDataBean.class.isAssignableFrom(clazz)) {


                    Class<? extends IDataBean> dataClazz = (Class<? extends IDataBean>) clazz;
                    Dao subDao;
                    try {
                        subDao = ormHelper.getDao(dataClazz);
                    } catch (SQLException e) {
                        return false;
                    }
                    field.setAccessible(true);
                    try {
                        IDataBean dirtyData = (IDataBean) field.get(data);
                        Field idField = ObjectHelper.findFieldWithAnnotation(clazz, Id.class);
                        idField.setAccessible(true);

                        Object id = idField.get(dirtyData);
                        if (subDao.idExists(id)) {
                            IDataBean dataInDb = (IDataBean) subDao.queryForId(id);

                            if (dataInDb.isComplete()) {
                                //不用不完整的数据覆盖完整数据，不做操作。
                            } else {
                                //合并数据
                                ObjectHelper.merge(dirtyData, dataInDb);
                            }
                            subDao.update(dataInDb);
                        } else {
                            subDao.create(dirtyData);
                        }
                        //新增到foreign-key对应的表。
                    } catch (SQLException e) {
                        //如果已经存在，不处理
                        return false;
                    } catch (IllegalAccessException e) {
                        return false;
                    } catch (IllegalArgumentException e) {
                        return false;
                    }

                }

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return true;
    }
}
