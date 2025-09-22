package utils.validation;

// Validator interface for validating data for service classes.
// Strategy pattern to allow different validation implementations.
public interface Validator<T> {
    void validate(T data) throws IllegalArgumentException;
}
