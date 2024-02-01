package com.mapwithplan.mapplan.mock;

import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.beans.PropertyEditor;
import java.util.List;
import java.util.Map;

public class FakeBindingResult implements BindingResult {
    @Override
    public Object getTarget() {
        return null;
    }

    @Override
    public Map<String, Object> getModel() {
        return null;
    }

    @Override
    public Object getRawFieldValue(String field) {
        return null;
    }

    @Override
    public PropertyEditor findEditor(String field, Class<?> valueType) {
        return null;
    }

    @Override
    public PropertyEditorRegistry getPropertyEditorRegistry() {
        return null;
    }

    @Override
    public String[] resolveMessageCodes(String errorCode) {
        return new String[0];
    }

    @Override
    public String[] resolveMessageCodes(String errorCode, String field) {
        return new String[0];
    }

    @Override
    public void addError(ObjectError error) {

    }

    @Override
    public String getObjectName() {
        return null;
    }

    @Override
    public void reject(String errorCode, Object[] errorArgs, String defaultMessage) {

    }

    @Override
    public void rejectValue(String field, String errorCode, Object[] errorArgs, String defaultMessage) {

    }

    @Override
    public List<ObjectError> getGlobalErrors() {
        return null;
    }

    @Override
    public List<FieldError> getFieldErrors() {
        return null;
    }

    @Override
    public Object getFieldValue(String field) {
        return null;
    }
}
