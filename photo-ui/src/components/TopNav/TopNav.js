import { React, useEffect} from 'react';
import { useNavigate } from 'react-router-dom';

const TopNav = () => {
    const navigate = useNavigate();
    const isLoggedIn = !!localStorage.getItem('jwt');
    console.log('isLoggedIn', isLoggedIn);

    const handleLogin = () => {
        if (isLoggedIn) {
            localStorage.removeItem('jwt');
            navigate('/login');
        } else {
            navigate('/login');
        }
    };

    useEffect(() => {
        if (!isLoggedIn) {
            navigate('/login');
        }
    }, [isLoggedIn, navigate]);

    return (
        <div className='topnav'>
            <div className="logo">Website Logo</div>
            <nav>
                <ul>
                    <li><a href="/">Home</a></li>
                    <li><a href="/about">About</a></li>
                    <li><a href="/contact">Contact</a></li>
                    <li><a href="/login" className="login-button" onClick={handleLogin}>Login</a></li>
                </ul>
            </nav>
        </div>
    );
};

export default TopNav;