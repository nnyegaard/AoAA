package org.givwenzen.reflections.adapters;

import java.util.List;
import java.io.InputStream;
import java.io.IOException;

/**
 *
 */
public interface MetadataAdapter<C,F,M> {

    //
    String getClassName(final C cls);

    String getSuperclassName(final C cls);

    List<String> getInterfacesNames(final C cls);

    //
    List<F> getFields(final C cls);

    List<M> getMethods(final C cls);

    String getMethodName(final M method);

    List<String> getParameterNames(final M method);

    List<String> getClassAnnotationNames(final C aClass);

    List<String> getFieldAnnotationNames(final F field);

    List<String> getMethodAnnotationNames(final M method);

    List<String> getParameterAnnotationNames(final M method, final int parameterIndex);

    String getReturnTypeName(final M method);

    String getFieldName(final F field);

    C create(InputStream inputStream) throws IOException;

    String getMethodModifier(M method);

    String getMethodKey(C cls, M method);
}
