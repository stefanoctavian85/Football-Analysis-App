import { useAuth } from '../../hooks/useAuth';
import './Navbar.css';
import { useNavigate } from 'react-router-dom';

const pages = [{
    name: 'Matches', href: '/matches',
}, {
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
                        <a href='/'>
                            <p>Footprint</p>
                        </a>
                    </div>

                    <div className='navbar-center'>
                        <ul className='navbar-center-list'>
                            {
                                pages.map((element, index) => (
                                    <li key={index} className='navbar-center-list-item'>
                                        <a href={element.href}>
                                            {element.name}
                                        </a>
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