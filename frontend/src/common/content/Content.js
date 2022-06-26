import React, {Component} from "react";
import "./Content.css"
import {Button, Col, Container, Form, FormControl} from "react-bootstrap";
import {Formik} from "formik";
import {toast} from "react-toastify";
import { VALIDATION_SEARCH } from "../../model/auth/validatonShema";
import ItemList from "../../model/item/ItemList";

class Content extends Component {
    constructor() {
        super();
        this.state = {
            searchOptions: null,
            arrayItems: []
        }
        //localStorage.clear()
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
                        validationSchema ={VALIDATION_SEARCH}
                        onSubmit={(values, {setSubmitting, resetForm}) => {
                            console.log(JSON.stringify(values));
                            if (localStorage.getItem('Session') == null) {
                                toast.error(
                                    "Авторизируйтесь в систему!",
                                    {position: toast.POSITION.BOTTOM_LEFT}
                                )
                            } else {
                                debugger
                                this.state.arrayItems = []
                                this.state.arrayItems.push(
                                    <ItemList searchOptions={values}/>
                                )
                                //this.changeSearchOptions(values);
                            }
                        }}
                    >
                        {({
                              values,
                              handleChange,
                              handleBlur,
                              handleSubmit
                          }) => (
                            <div>
                                <h4>Поиск</h4>
                                <Form className={"search-form"} onSubmit={handleSubmit}>
                                    <Form.Row>
                                        <Form.Group as={Col}>
                                            <FormControl
                                                type="text"
                                                className="mr-sm-2"
                                                name="name"
                                                placeholder="Гильза"
                                                onChange={handleChange}
                                                onBlur={handleBlur}
                                                value={values.request}
                                            />
                                        </Form.Group>
                                        <Form.Group as={Col}>
                                            <FormControl
                                                type="text"
                                                className="mr-sm-2"
                                                name="id"
                                                placeholder="P181*"
                                                onChange={handleChange}
                                                onBlur={handleBlur}
                                                value={values.request}
                                            />
                                        </Form.Group>
                                        <Col>
                                            <Button variant="outline-success"  type="submit">Найти</Button>
                                        </Col>
                                    </Form.Row>
                                    <Form.Row>
                                        <Form.Group as={Col} sm={4}>
                                            <Form.Label><h5>Тип поиска</h5></Form.Label>
                                            <Form.Control
                                                as="select"
                                                name="search"
                                                onBlur={handleBlur}
                                                value={values.category}
                                                onChange={handleChange}
                                            >
                                                <option value="11" data-marker="option(9)">Item_Name_or_ID</option>
                                                <option value="12" data-marker="option(14)">RoleByName</option>
                                                <option value="13" data-marker="option(81)">GroupByName</option>
                                                <option value="14" data-marker="option(11)">__Item_Revision_name_or_ID</option>
                                                <option value="15" data-marker="option(10)">__WorklistUserPoolSchedTasks</option>
                                                <option value="21" data-marker="option(24)">Audit - Project Based Logs</option>
                                                <option value="22" data-marker="option(23)">Items_ref_by_ReportDefinition</option>
                                                <option value="23" data-marker="option(25)">__Find_owning_groups_of_given_role</option>
                                                <option value="24" data-marker="option(85)">__EventTypeMapping</option>
                                                <option value="25" data-marker="option(26)">Audit - Workflow Detailed</option>
                                                <option value="26" data-marker="option(42)">Audit - Workflow Summary</option>
                                                <option value="27" data-marker="option(86)">Audit - Workflow General</option>
                                                <option value="31" data-marker="option(111)">Audit - Workflow Signoff</option>
                                                <option value="32" data-marker="option(112)">Material Management - Substances</option>
                                            </Form.Control>
                                        </Form.Group>
                                    </Form.Row>
                                </Form>
                            </div>
                        )}
                    </Formik>
                </Container>
                {
                    this.state.arrayItems
                }
            </Container>
        )
    }
}

export default Content;