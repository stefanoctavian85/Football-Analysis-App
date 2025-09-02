import './Login.css';
import { useEffect, useState } from 'react';
import { useAuth } from '../../hooks/useAuth.jsx';
import { useNavigate } from 'react-router-dom';

const emailRegex = /^[a-zA-Z0-9._%+-]+@([a-zA-Z0-9-]+\.)+[a-zA-Z]{2,}$/;

function Login() {
    const { user, accessToken, login } = useAuth();

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const navigate = useNavigate();

    useEffect(() => {
        if (user && accessToken) {
            navigate('/');
        }
    }, [user, accessToken]);

    const areCredentialsValid = (email, password) => {
        if (!emailRegex.test(email)) {
            setErrorMessage('Invalid email address!');
            return false;
        }

        if (password.length < 6 || password.length > 72) {
            setErrorMessage('Password length must be between 6 and 72 characters.');
            return false;
        }

        setErrorMessage('');
        return true;
    }

    async function handleLogin(e) {
        e.preventDefault();

        try {
            if (!areCredentialsValid(email, password)) {
                return;
            }

            await login(email, password);
        } catch (error) {
            setErrorMessage(error.message);
            console.error('Login component error: ', error);
        }
    }

    return (
        <div className='login-page'>
            <div className='login-card'>
                <div className='login-header'>
                    <p className='login-header-title'>Log in</p>
                </div>

                <div className='login-content'>
                    <form onSubmit={handleLogin} method='post' className='login-form'>
                        <div className='login-input'>
                            <label htmlFor='email-input'>Email</label>
                            <input
                                id='email-input'
                                name='email-input'
                                type='email'
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                        </div>

                        <div className='login-input'>
                            <label htmlFor='password-input'>Password</label>
                            <input
                                id='password-input'
                                name='password-input'
                                type='password'
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>

                        <div className='login-input'>
                            <button type='submit'>Log in</button>
                        </div>
                    </form>
                </div>

                <div className='login-redirect-register'>
                    <p>Don't have an account?</p>
                    <a href='/register'>Register now!</a>
                </div>
            </div>

            <div>
                {errorMessage}
            </div>
        </div>
    );
}

export default Login;