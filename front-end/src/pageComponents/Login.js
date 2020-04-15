import React, { Component } from 'react';
import { UserContext } from '../components/UserContext';
import apiUrl from '../apiUrl';

/**
 *
 */
export default class Login extends Component {

    static contextType = UserContext;
    username = React.createRef();
    password = React.createRef();

    handleSubmit = event => {

        console.log('ref to username: ', this.username.current);

        const u = this.username.current.value;
        const p = this.password.current.value;

        console.log('Submitting...', u, p);

        fetch(`${apiUrl}/login`, {
            mode: 'cors',
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams({
                username: u,
                password: p,
            }),
        })
            .then(response => response.json())
            .then(json => {
                console.log(json);

                // Store the user's data in local storage to make them available
                // for the next user's visit.
                localStorage.setItem('token', json.token);
                localStorage.setItem('username', u);

                // Use the setUserData function available through the UserContext.
                this.context.setUserData(json.token, u);

                // Use the history prop available through the Route to programmatically
                // navigate to another route.
                this.props.history.push('/main');
            });

        event.preventDefault();
    };

    render() {

        return (
            <form onSubmit={this.handleSubmit} className="d-flex justify-content-center align-items-center mt-3 mb-3">
                <div className="form-group">
                    <label>
                        Username <input type="text" ref={this.username} className="form-control" />
                    </label>
                </div>
                <div className="form-group ml-2">
                    <label>
                        Password <input type="password" ref={this.password} className="form-control" />
                    </label>
                </div>
                <button type="submit" className="btn btn-primary ml-2">Login</button>
            </form>
        );
    }

};
