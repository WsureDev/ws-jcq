package top.wsure.bot.utils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import sun.reflect.ConstructorAccessor;
import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;
import top.wsure.bot.common.cache.CacheManagerImpl;
import top.wsure.bot.common.cache.EntityCache;
import top.wsure.bot.common.enums.CacheEnum;

public class DynamicEnumUtils {

   private static final ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();

   private static void setFailsafeFieldValue(Field field, Object target, Object value) throws NoSuchFieldException,
           IllegalAccessException {

       // let's make the field accessible
       field.setAccessible(true);

       // next we change the modifier in the Field instance to
       // not be final anymore, thus tricking reflection into
       // letting us modify the static final field
       Field modifiersField = Field.class.getDeclaredField("modifiers");
       modifiersField.setAccessible(true);
       int modifiers = modifiersField.getInt(field);

       // blank out the final bit in the modifiers int
       modifiers &= ~Modifier.FINAL;
       modifiersField.setInt(field, modifiers);

       FieldAccessor fa = reflectionFactory.newFieldAccessor(field, false);
       fa.set(target, value);
   }

   private static void blankField(Class<?> enumClass, String fieldName) throws NoSuchFieldException,
           IllegalAccessException {
       for (Field field : Class.class.getDeclaredFields()) {
           if (field.getName().contains(fieldName)) {
               AccessibleObject.setAccessible(new Field[] { field }, true);
               setFailsafeFieldValue(field, enumClass, null);
               break;
           }
       }
   }

   private static void cleanEnumCache(Class<?> enumClass) throws NoSuchFieldException, IllegalAccessException {
       blankField(enumClass, "enumConstantDirectory"); // Sun (Oracle?!?) JDK 1.5/6
       blankField(enumClass, "enumConstants"); // IBM JDK
   }

   private static ConstructorAccessor getConstructorAccessor(Class<?> enumClass, Class<?>[] additionalParameterTypes)
           throws NoSuchMethodException {
       Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
       parameterTypes[0] = String.class;
       parameterTypes[1] = int.class;
       System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
       return reflectionFactory.newConstructorAccessor(enumClass.getDeclaredConstructor(parameterTypes));
   }

   private static Object makeEnum(Class<?> enumClass, String value, int ordinal, Class<?>[] additionalTypes,
           Object[] additionalValues) throws Exception {
       Object[] params = new Object[additionalValues.length + 2];
       params[0] = value;
       params[1] = ordinal;
       System.arraycopy(additionalValues, 0, params, 2, additionalValues.length);
       return enumClass.cast(getConstructorAccessor(enumClass, additionalTypes).newInstance(params));
   }

   /**
    * Add an enum instance to the enum class given as argument
    *
    * @param <T> the type of the enum (implicit)
    * @param enumType the class of the enum to be modified
    * @param enumName the name of the new enum instance to be added to the class.
    */
   @SuppressWarnings("unchecked")
   public static <T extends Enum<?>> void addEnum(Class<T> enumType, String enumName,Class<?>[] classes,Object[] args) {

       // 0. Sanity checks
       if (!Enum.class.isAssignableFrom(enumType)) {
           throw new RuntimeException("class " + enumType + " is not an instance of Enum");
       }

       // 1. Lookup "$VALUES" holder in enum class and get previous enum instances
       Field valuesField = null;
       Field[] fields = enumType.getDeclaredFields();
       for (Field field : fields) {
           if (field.getName().contains("$VALUES")) {
               valuesField = field;
               break;
           }
       }
       AccessibleObject.setAccessible(new Field[] { valuesField }, true);

       try {

           // 2. Copy it
           T[] previousValues = (T[]) valuesField.get(enumType);
           List<T> values = new ArrayList<T>(Arrays.asList(previousValues));

           // 3. build new enum
           T newValue = (T) makeEnum(enumType, // The target enum class
                   enumName, // THE NEW ENUM INSTANCE TO BE DYNAMICALLY ADDED
                   values.size(),
                   classes, // could be used to pass values to the enum constuctor if needed
                   args); // could be used to pass values to the enum constuctor if needed

           // 4. add new value
           values.add(newValue);

           // 5. Set new values field
           setFailsafeFieldValue(valuesField, null, values.toArray((T[]) Array.newInstance(enumType, 0)));

           // 6. Clean enum cache
           cleanEnumCache(enumType);

       } catch (Exception e) {
           e.printStackTrace();
           throw new RuntimeException(e.getMessage(), e);
       }
   }

    /**
     * TestEnum And Test Main
     */
   @AllArgsConstructor
   @Getter
   @ToString
   private static enum TestEnum {
       a("aa"),
       b("bb"),
       c("cc");
       private String test;
   };

   public static void main(String[] args) {

       // Dynamically add 3 new enum instances d, e, f to TestEnum
       addEnum(TestEnum.class, "d",new Class[]{String.class},new Object[]{"dd"});
       addEnum(TestEnum.class, "e",new Class[]{String.class},new Object[]{"ee"});
       addEnum(TestEnum.class, "f",new Class[]{String.class},new Object[]{"ff"});

       addEnum(CacheEnum.class,"TestCache",
               new Class[]{String.class},
               new Object[]{"test"});
       CacheManagerImpl cache = CacheEnum.getManager("test");
       cache.putCache("11","22",100L);
       System.out.println(cache.getCacheDataByKey("11").toString());

       // Run a few tests just to show it works OK.
       System.out.println(Arrays.deepToString(TestEnum.values()));
       // Shows : [a, b, c, d, e, f]
   }
}
