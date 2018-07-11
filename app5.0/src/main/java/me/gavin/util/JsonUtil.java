package me.gavin.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

/**
 * Gson 工具类
 * <p>
 * | @Expose 过滤字段：serialize和deserialize。默认都是true。 excludeFieldsWithoutExposeAnnotation 后有效
 * | transient Java关键字? 永久地关闭转换(包括其他?)
 * <p>
 * L.e(JsonUtil.toList(json1, new TypeToken<ArrayList<User>>() {}));
 * L.e(JsonUtil.toList2(json1, User[].class));
 *
 * @author gavin.xiong 2016/12/6
 */
public class JsonUtil {

    private static Gson gson;

    private static void init() {
        gson = new GsonBuilder()
//                .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性
//                .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式
//                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")//时间转化为特定格式
//                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效.
//                .setPrettyPrinting() //对json结果格式化.
//                .setVersion(1.0)
//                .disableHtmlEscaping()//默认是GSON把HTML 转义的，但也可以设置不转义
//                .serializeNulls()//把null值也转换，默认是不转换null值的，可以选择也转换,为空时输出为{a:null}，而不是{}
                .create();
    }

    public static <T> T toObj(String json, Class<T> clazz) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        if (gson == null) {
            init();
        }
        try {
            L.d(json);
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        if (gson == null) {
            init();
        }
        String str = gson.toJson(object);
        L.d(str);
        return str;
    }

    /**
     * List<Object> list = JsonUtil.toList(json, new TypeToken<ArrayList<Object>>(){});
     */
    public static <T> List<T> toList(String json, TypeToken typeToken) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        if (gson == null) {
            init();
        }
        try {
            return gson.fromJson(json, typeToken.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * List<Object> list = JsonUtil.toList2(json1, User[].class);
     */
    public static <T> List<T> toList2(String json, Class<T[]> clazz) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        if (gson == null) {
            init();
        }
        try {
            return Arrays.asList(gson.fromJson(json, clazz));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
