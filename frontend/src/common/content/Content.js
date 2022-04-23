import React, {Component} from "react";
import "./Content.css"
import {Button, Col, Container, Form, FormControl} from "react-bootstrap";
import {Formik} from "formik";

class Content extends Component {
    constructor() {
        super();
        this.state = {
            searchOptions: null,
        }
        this.changeSearchOptions = this.changeSearchOptions.bind(this);
    }

    changeSearchOptions(searchOptions) {
        this.setState({
            searchOptions: searchOptions
        });
    }

    render() {
        return (
            <Container className="main-content" style={{marginTop: "1em"}}>
                <Container className={"search"}>
                    <Formik
                        initialValues={{name: "", id: ""}}
                        
                        onSubmit={(values, {setSubmitting, resetForm}) => {
                            console.log(JSON.stringify(values));
                            this.changeSearchOptions(values);
                        }}
                    >
                        {({
                              values,
                              handleChange,
                              handleBlur,
                              handleSubmit
                          }) => (
                            <div>
                                <h4>Поиск деталей</h4>
                                <Form className={"search-form"} onSubmit={handleSubmit}>
                                    <Form.Row>
                                        <Form.Group as={Col}>
                                            <FormControl
                                                type="text"
                                                className="mr-sm-2"
                                                name="query"
                                                placeholder="Например, Лопасти"
                                                onChange={handleChange}
                                                onBlur={handleBlur}
                                                value={values.request}
                                            />
                                        </Form.Group>
                                        <Col>
                                            <Button variant="outline-success"  type="submit">Найти</Button>
                                        </Col>
                                    </Form.Row>
                                </Form>
                            </div>
                        )}
                    </Formik>
                </Container>
                {
                    
                }
            </Container>
        )
    }
}

export default Content;