package org.backend.server.microservices.authorization.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

public class PasswordValidationException extends ConstraintViolationException {
    public PasswordValidationException(String message) {
        super(message, createViolation(message));
    }

    private static Set<ConstraintViolation<?>> createViolation(String message) {
        ConstraintViolation<?> violation = new ConstraintViolation<Object>() {
            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public Path getPropertyPath() {
                return new Path() {
                    @Override
                    public Iterator<Node> iterator() {
                        return Collections.emptyIterator();
                    }

                    @Override
                    public String toString() {
                        return "password";
                    }
                };
            }

            // Other required methods with default implementations
            @Override
            public String getMessageTemplate() {
                return message;
            }

            @Override
            public Object getRootBean() {
                return null;
            }

            @Override
            public Class<Object> getRootBeanClass() {
                return null;
            }

            @Override
            public Object getLeafBean() {
                return null;
            }

            @Override
            public Object[] getExecutableParameters() {
                return null;
            }

            @Override
            public Object getExecutableReturnValue() {
                return null;
            }

            @Override
            public Object getInvalidValue() {
                return null;
            }

            @Override
            public ConstraintDescriptor<?> getConstraintDescriptor() {
                return null;
            }

            @Override
            public <U> U unwrap(Class<U> type) {
                return null;
            }
        };

        return Collections.singleton(violation);
    }
}