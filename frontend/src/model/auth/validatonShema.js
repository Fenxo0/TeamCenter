import * as Yup from "yup";
import {PASSWORD_MAX_LENGTH, PASSWORD_MIN_LENGTH} from "../../constants";

export const VALIDATION_LOGIN_SCHEMA = Yup.object().shape({
    username: Yup.string()
        .min(PASSWORD_MIN_LENGTH, "Логин должен состоять не меньше, чем из " + PASSWORD_MIN_LENGTH + " символов")
        .max(PASSWORD_MAX_LENGTH, "Логин должен состоять не больше, чем из " + PASSWORD_MAX_LENGTH + " символов")
        .required("*Это обязательное поле"),
    password: Yup.string()
        .min(PASSWORD_MIN_LENGTH, "Пароль должен содержать не менее " + PASSWORD_MIN_LENGTH + " символов")
        .max(PASSWORD_MAX_LENGTH, "Пароль должен содержать не более " + PASSWORD_MAX_LENGTH + " символов")
        .required("*Это обязательное поле")
});

export const VALIDATION_SEARCH = Yup.object().shape({
    id: Yup.string()
        .min(PASSWORD_MIN_LENGTH, "Id должен состоять не меньше, чем из одного символа"),
    password: Yup.string()
        .min(PASSWORD_MIN_LENGTH, "Имя должно состоять не меньше, чем из одного символа")
});