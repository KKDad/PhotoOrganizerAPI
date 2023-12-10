import React from 'react';


const Header = () => {
    const handleLogin = () => {
        // Add your login logic here
    };   
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