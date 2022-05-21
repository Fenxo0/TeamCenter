import { Component } from "react";
import { Alert } from "react-bootstrap";
import { executeSavedQueries } from "../../util/APIUtils";

class ItemList extends Component {
    constructor() {
        super();
        this.state = {
            items: "",
            page: 0,
            size: 10,
            totalElements: 0,
            totalPages: 0,
            last: true,
            currentVotes: [],
            isLoading: false
        };
        this.loadAllUsers = this.loadAllUsers.bind(this);
        this.runHolder = this.runHolder;
        this.loadItemList = this.loadItemList.bind(this);
        this.handleLoadMore = this.handleLoadMore.bind(this);
    }

    loadAllUsers(page = 0, size = USER_LIST_SIZE) {
        let promise;
        promise = getAllUsers(page, size);

        if(!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });

        promise
            .then(response => {
                const users = this.state.users.slice();

                this.setState({
                    users: users.concat(response.content),
                    page: response.page,
                    size: response.size,
                    totalElements: response.totalElements,
                    totalPages: response.totalPages,
                    last: response.last,
                    isLoading: false
                })
                console.log(this.state.users);
            }).catch(error => {
            this.setState({
                isLoading: false
            })
        });
    }

    loadItemList() {
        let promise;

        if (this.props.searchOptions) {
            promise = executeSavedQueries(this.props.searchOptions);
        }
        if (!promise) {
            return;
        }

        //this.setState({
        //    isLoading: true
        //});

        promise
            .then(response => {

                this.setState({
                    items: items.concat(response.content),
                    isLoading: false
                })
            }).catch(error => {
            this.setState({
                isLoading: false
            })
        });
    }

    handleLoadMore() {
        this.loadItemList(this.state.page + 1);
    }

    componentDidUpdate(nextProps) {
        debugger
        if (localStorage.getItem("Session") === null) {
            // Reset State
            this.setState({
                items: [],
                page: 0,
                size: 10,
                totalElements: 0,
                totalPages: 0,
                last: true,
                currentVotes: [],
                isLoading: false
            });
            return
        }
        this.loadItemList();
    }

    render() {
        return (
            <div className="my-items">
                {this.state.items}
                {
                !this.state.isLoading && this.state.items.length === 0 ? (
                    <div className="no-items-found">
                        <Alert variant="success">
                            <Alert.Heading>Детали</Alert.Heading>
                            <p>
                                У вас нет ни одной детали
                            </p>
                        </Alert>
                    </div>
                ) : null
            }
            </div>
        )
    }
}

export default ItemList;