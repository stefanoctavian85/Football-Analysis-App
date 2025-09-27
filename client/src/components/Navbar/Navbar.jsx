import { useAuth } from '../../hooks/useAuth';
import './Navbar.css';
import { useNavigate, Link } from 'react-router-dom';

const pages = [{
    name: 'Players', href: '/players',
}, {
    name: 'Standings', href: '/standings',
}]

function Navbar() {
    const navigate = useNavigate();

    const { user, accessToken, logout } = useAuth();

    function accountButtonRedirect() {
        if (user) {
            logout();
        } else {
            navigate('/login');
        }
    }

    return (
        <div className='navbar-page'>
            <header>
                <nav className='navbar-content'>
                    <div className='navbar-left'>
                        <Link to='/'>
                            <p>Footprint</p>
                        </Link>
                    </div>

                    <div className='navbar-center'>
                        <ul className='navbar-center-list'>
                            {
                                pages.map((element, index) => (
                                    <li key={index} className='navbar-center-list-item'>
                                        <Link to={element.href}>
                                            {element.name}
                                        </Link>
                                    </li>
                                ))
                            }
                        </ul>
                    </div>

                    <div className='navbar-right'>
                        <button
                            onClick={accountButtonRedirect}
                        >
                            { user ? 'Log out' : 'Log in' }
                        </button>
                    </div>
                </nav>
            </header>
        </div>
    )
}

export default Navbar;