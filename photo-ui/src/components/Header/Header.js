import React from 'react';
import { useNavigate } from 'react-router-dom';

const Header = () => {
    const navigate = useNavigate();
    const isLoggedIn = !!localStorage.getItem('jwt');

    const handleLogin = () => {
        if (isLoggedIn) {
            localStorage.removeItem('jwt');
            navigate('/login');
        } else {
            navigate('/login');
        }
    };

    if (!isLoggedIn) {
        history.push('/login');
    }

    return (
        <header>
            <div className="logo">Website Logo</div>
            <nav>
                <ul>
                    <li><a href="/">Home</a></li>
                    <li><a href="/about">About</a></li>
                    <li><a href="/contact">Contact</a></li>
                    <li><a href="/login" className="login-button" onClick={handleLogin}>Login</a></li>
                </ul>
            </nav>
        </header>
    );
};

export default Header;