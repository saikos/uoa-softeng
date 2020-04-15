import React, { Component } from 'react';
import { UserContext } from '../components/UserContext';
import apiUrl from '../apiUrl';

/**
 *
 */
export default class Logout extends Component {
    
    static contextType = UserContext;
    
    doLogout() {

        localStorage.removeItem('token');
        localStorage.removeItem('username');
                    
        this.context.setUserData(null, null);
        
        this.props.history.push('/');
    }
    
    componentDidMount() {

        // Perform an ajax call to logout and then clean up local storage and context state.

        fetch(`${apiUrl}/logout`, {
            mode: 'cors',
            method: 'POST',
            headers: {
                'X-CONTROL-CENTER-AUTH': this.context.username,
                'Content-Type': 'application/x-www-form-urlencoded',
            },
        })
            .then(() => this.doLogout());
    }
    
    render() {

        return (<h2>Logging out...</h2>);
    }

};
