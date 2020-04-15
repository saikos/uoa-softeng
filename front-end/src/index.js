import React from 'react';
import ReactDOM from 'react-dom';
import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import App from './App';

const userData = {
    token: localStorage.getItem('token'),
    username: localStorage.getItem('username'),
};

ReactDOM.render(
    <React.StrictMode>
        <App userData={userData} />
    </React.StrictMode>,
    document.getElementById('root')
);
