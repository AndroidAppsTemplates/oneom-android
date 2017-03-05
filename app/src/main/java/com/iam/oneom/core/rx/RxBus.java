package com.iam.oneom.core.rx;

import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public enum RxBus {

    INSTANCE;

    private static final String TAG = RxBus.class.getSimpleName();
    private final Subject<Object, Object> mBus = new SerializedSubject<>(PublishSubject.create());

    @SuppressWarnings("unchecked")
    public <T> Subscription register(final Class<T> eventType, Action1<T> onNext) {
        return mBus
                .observeOn(AndroidSchedulers.mainThread())
                .filter(o -> eventType.isInstance(o))
                .map(o -> (T) o)
                .subscribe(onNext, throwable -> {
                    Log.e(TAG, "", throwable);
                });
    }

    @SuppressWarnings("unchecked")
    public <T> Subscription register(final Class<T> eventType, Func1<T, Boolean> filter, Action1<T> onNext) {
        return mBus
                .observeOn(AndroidSchedulers.mainThread())
                .filter(o -> eventType.isInstance(o) && filter.call((T) o))
                .map(o -> (T) o)
                .subscribe(onNext, throwable -> {
                    Log.e(TAG, "", throwable);
                });
    }

    @SuppressWarnings("unchecked")
    public <T> Observable<T> observe(final Class<T> eventType) {
        return mBus
                .observeOn(AndroidSchedulers.mainThread())
                .filter(o -> eventType.isInstance(o))
                .map(o -> (T) o);
    }

    @SuppressWarnings("unchecked")
    public <T> Observable<T> observe(final Class<T> eventType, Func1<T, Boolean> filter) {
        return mBus
                .observeOn(AndroidSchedulers.mainThread())
                .filter(o -> eventType.isInstance(o) && filter.call((T) o))
                .map(o -> (T) o);
    }

    /**
     * Works only for generic type with 1 parameter
     *
     * @return type
     */
    public static Type resolveGenericType(Object object) {
        if (object != null) {
            Class<?> clazz = object.getClass();
            Type type = clazz.getGenericSuperclass();
            while (!(type instanceof ParameterizedType) && !(Object.class.equals(clazz))) {
                clazz = clazz.getSuperclass();
                type = clazz.getGenericSuperclass();
            }

            if (type instanceof ParameterizedType) {
                return ((ParameterizedType) type).getActualTypeArguments()[0];
            }
            return Object.class;
        } else {
            return null;
        }
    }

    public static Class<?> getRawType(Object object) {
        return getRawType(resolveGenericType(object));
    }

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class<?>) {
            // type is a normal class.
            return (Class<?>) type;

        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            // I'm not exactly sure why getRawType() returns Type instead of Class.
            // Neal isn't either but suspects some pathological case related
            // to nested classes exists.
            Type rawType = parameterizedType.getRawType();
            return (Class<?>) rawType;

        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();

        } else if (type instanceof TypeVariable) {
            // we could use the variable's bounds, but that won't work if there are multiple.
            // having a raw type that's more general than necessary is okay
            return Object.class;

        } else if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);

        } else {
            return null;
        }
    }

    private class ClassNotResolvedException extends RuntimeException {
        public ClassNotResolvedException(String name) {
            super(name);
        }
    }

    public void post(Object event) {
        mBus.onNext(event);
    }

    public static abstract class Action<T> {
        public abstract void call(T t);
    }
}