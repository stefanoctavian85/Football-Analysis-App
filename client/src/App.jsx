import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom'; 
import Home from './components/Home/Home.jsx';
import Navbar from './components/Navbar/Navbar.jsx';
import Login from './components/Login/Login.jsx';
import Register from './components/Register/Register.jsx'
import { AuthProvider } from './contexts/AuthContext.jsx';

function App() {
  return (
    <AuthProvider className='app'>
      <BrowserRouter>
        <Navbar />

        <Routes>
          <Route>
            <Route path='/' element={<Home />}></Route>
          </Route>

          <Route>
            <Route path='/login' element={<Login />}></Route>
          </Route>

          <Route>
            <Route path='/register' element={<Register />}></Route>
          </Route>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}

export default App
