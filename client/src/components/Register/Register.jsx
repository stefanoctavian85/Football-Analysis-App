import './Register.css';
import { useEffect, useState } from 'react';
import { useAuth } from '../../hooks/useAuth.jsx';
import { useNavigate } from 'react-router-dom';

const emailRegex = /^[a-zA-Z0-9._%+-]+@([a-zA-Z0-9-]+\.)+[a-zA-Z]{2,}$/;

function Register() {
    const { user, accessToken, register } = useAuth();

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const navigate = useNavigate();

    useEffect(() => {
        if (user && accessToken) {
            navigate('/');
        }
    }, [user, accessToken]);

    const areCredentialsValid = (email, firstName, lastName, password) => {
        if (!emailRegex.test(email)) {
            setErrorMessage('Invalid email address!');
            return false;
        }

        if (firstName.length < 3 || firstName.length > 20) {
            setErrorMessage('First name length must be between 3 and 20 characters.');
            return false;
        }

        if (lastName.length < 3 || lastName.length > 20) {
            setErrorMessage('Last name length must be between 3 and 20 characters.');
            return false;
        }

        if (password.length < 6 || password.length > 72) {
            setErrorMessage('Password length must be between 6 and 72 characters.');
            return false;
        }

        setErrorMessage('');
        return true;
    }

    async function handleRegister(e) {
        e.preventDefault();

        try {
            if (!areCredentialsValid(email, firstName, lastName, password)) {
                return;
            }

            await register(email, firstName, lastName, password);
        } catch (error) {
            setErrorMessage(error.message);
            console.error('Register component error: ', error);
        }
    }

    return (
        <div className='register-page'>
            <div className='register-card'>
                <div className='register-header'>
                    <p className='register-header-title'>Register</p>
                </div>

                <div className='register-content'>
                    <form onSubmit={handleRegister} method='post' className='register-form'>
                        <div className='register-input'>
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

                        <div className='register-input'>
                            <label htmlFor='firstname-input'>First name</label>
                            <input
                                id='firstname-input'
                                name='firstname-input'
                                type='text'
                                value={firstName}
                                onChange={(e) => setFirstName(e.target.value)}
                                required
                            />
                        </div>

                        <div className='register-input'>
                            <label htmlFor='lastname-input'>Last name</label>
                            <input
                                id='lastname-input'
                                name='lastname-input'
                                type='text'
                                value={lastName}
                                onChange={(e) => setLastName(e.target.value)}
                                required
                            />
                        </div>

                        <div className='register-input'>
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

                        <div className='register-input'>
                            <button type='submit'>Log in</button>
                        </div>
                    </form>
                </div>

                <div className='register-redirect-login'>
                    <p>Already have an account?</p>
                    <a href='/login'>Login now!</a>
                </div>
            </div>

            <div>
                {errorMessage}
            </div>
        </div>
    );
}

export default Register;