import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext.jsx';
import { AppProvider } from './contexts/AppContext.jsx';
import Home from './components/Home/Home.jsx';
import Navbar from './components/Navbar/Navbar.jsx';
import Login from './components/auth/Login/Login.jsx';
import Register from './components/auth/Register/Register.jsx';
import Standings from './components/football/Standings/Standings.jsx';
import Championship from './components/football/championship/Championship.jsx';
import Results from './components/football/Results/Results.jsx';
import Match from './components/football/Match/Match.jsx';

function App() {
  return (
    <AppProvider>
      <AuthProvider>
        <BrowserRouter>
          <Navbar />
          <Routes>
            <Route path='/' element={<Home />}></Route>
            <Route path='/login' element={<Login />}></Route>
            <Route path='/register' element={<Register />}></Route>
            <Route path='/standings' element={<Standings />}></Route>
            <Route path='/standings/:competitionName' element={<Championship />}></Route>
            <Route path='/standings/:competitionName/:teamName' element={<Results />}></Route>
            <Route path='/match/:matchId' element={<Match />}></Route>
          </Routes>
        </BrowserRouter>
      </AuthProvider>
    </AppProvider>
  )
}

export default App
