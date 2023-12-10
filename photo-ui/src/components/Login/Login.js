import React, { useState } from 'react';
import { authenticateAndGetToken } from '../../api/apis/AuthControllerApi';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleUsernameChange = (event) => {
        setUsername(event.target.value);
    };

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };


    const handleSubmit = async (event) => {
        event.preventDefault();

        const response = await authenticateAndGetToken(username, password);
        console.log(response);

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('jwt', data.token);
            // Redirect the user to the home page
        } else {
            // Display an error message
        }
    };

    return (
        <div>
            <h2>Login</h2>
            <form onSubmit={handleSubmit}>
                <label>
                    Username:
                    <input type="text" value={username} onChange={handleUsernameChange} />
                </label>
                <label>
                    Password:
                    <input type="password" value={password} onChange={handlePasswordChange} />
                </label>
                <input type="submit" value="Submit" />
            </form>
        </div>
    );
};

export default Login;