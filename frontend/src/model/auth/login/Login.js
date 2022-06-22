import React, { Component } from 'react';
import './Login.css';
import {login} from "../../../util/APIUtils";
import {Button, Container, Form, Row} from "react-bootstrap";
import {toast} from "react-toastify";
import {Formik} from "formik";
import {VALIDATION_LOGIN_SCHEMA} from "../validatonShema";

class Login extends Component {
    render() {
        return (
            <div className="login-container">
                <div className="login-content block">
                    <h1 className="login-title">Авторизация</h1>
                    <LoginForm {...this.props} />
                </div>
            </div>
        );
    }
}

class LoginForm extends Component {
    render() {
        return (
            <Container>
                <Formik
                    initialValues={{username:"", password:""}}
                    validationSchema={VALIDATION_LOGIN_SCHEMA}
                    onSubmit={(values, {setSubmitting, resetForm}) => {
                        /*  // When button submits form and form is in the process of submitting, submit button is disabled
                          setSubmitting(true);*/
                        login(values)
                            .then(response => {
                                localStorage.setItem('Name', response.name);
                                localStorage.setItem('Group', response.group);
                                localStorage.setItem('HomeFolder', response.homeFolder);
                                localStorage.setItem('Project', response.project);
                                localStorage.setItem('Id', response.id);
                                localStorage.setItem('LicenseLevel', response.licenseLevel);
                                this.props.onLogin();
                            }).catch(error => {
                            if(error.status === 401) {
                                toast.error(
                                    "Некорректные данные. Попробуйте снова!",
                                    {position: toast.POSITION.BOTTOM_LEFT}
                                )
                                console.log("Your data is incorrect. Please try again!")
                            } else {
                                toast.error(
                                    error.message || "Что-то пошло не так. Попробуйте снова!",
                                    {position: toast.POSITION.BOTTOM_LEFT}
                                )
                                console.log(error.message || 'Sory! Something went wrong. Please try again!')
                            }
                        });
                    }}
                >
                    {( {values,
                           errors,
                           touched,
                           handleChange,
                           handleBlur,
                           handleSubmit,
                           isSubmitting }) => (
                        <Row className="justify-content-center">
                            <div className={"auth-form block"}>
                                <Form style={{width:"75%", justifyContent:"center"}} onSubmit={handleSubmit}>
                                    <Form.Group controlId="username" >
                                        <Form.Label>Имя пользователя</Form.Label>
                                        <Form.Control type="text"
                                                      placeholder="Введите имя пользователя"
                                                      name="username"
                                                      onChange={handleChange}
                                                      onBlur={handleBlur}
                                                      value={values.username}
                                                      className={touched.username && errors.username ? "has-error" : null}
                                        />
                                        {touched.username && errors.username ? (
                                            <div className="error-message">
                                                {errors.username}
                                            </div>
                                        ): null}
                                    </Form.Group>

                                    <Form.Group controlId="formBasicPassword">
                                        <Form.Label>Пароль</Form.Label>
                                        <Form.Control type="password"
                                                      placeholder="Введите пароль"
                                                      name="password"
                                                      onChange={handleChange}
                                                      onBlur={handleBlur}
                                                      value={values.password}
                                                      className={touched.password && errors.password ? "has-error" : null}
                                        />
                                        {touched.password && errors.password ? (
                                            <div className="error-message">{errors.password}</div>
                                        ): null}
                                    </Form.Group>
                                    <Form.Group controlId="group">
                                        <Form.Label>Группа</Form.Label>
                                        <Form.Control type="text"
                                                      placeholder="Группа"
                                                      name="group"
                                                      onChange={handleChange}
                                                      onBlur={handleBlur}
                                                      value={values.group}
                                                      className={touched.group && errors.group ? "has-error" : null}
                                        />
                                        {touched.group && errors.group ? (
                                            <div className="error-message">{errors.group}</div>
                                        ): null}
                                    </Form.Group>
                                    <Form.Group controlId="role">
                                        <Form.Label>Роль</Form.Label>
                                        <Form.Control type="text"
                                                      placeholder="Роль"
                                                      name="role"
                                                      onChange={handleChange}
                                                      onBlur={handleBlur}
                                                      value={values.role}
                                                      className={touched.role && errors.role ? "has-error" : null}
                                        />
                                        {touched.role && errors.role ? (
                                            <div className="error-message">{errors.role}</div>
                                        ): null}
                                    </Form.Group>
                                    <Form.Group controlId="role">
                                        <Form.Label>Сервер</Form.Label>
                                        <Form.Control type="text"
                                                      placeholder="Сервер"
                                                      name="server"
                                                      onChange={handleChange}
                                                      onBlur={handleBlur}
                                                      value={values.server}
                                                      className={touched.server && errors.server ? "has-error" : null}
                                        />
                                        {touched.server && errors.server ? (
                                            <div className="error-message">{errors.server}</div>
                                        ): null}
                                    </Form.Group>
                                    <Button style={{width: "100%"}} variant="primary" type="submit">
                                        Отправить
                                    </Button>
                                </Form>
                            </div>
                        </Row>
                    )}
                </Formik>
            </Container>
        );
    }
}

export default Login;
