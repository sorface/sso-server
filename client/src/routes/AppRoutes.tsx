import {FunctionComponent} from 'react';
import {Route, Routes} from 'react-router-dom';
import {pathnames} from '../constants';
import {Home} from '../pages/Home/Home';
import {NotFound} from '../pages/NotFound/NotFound';
import {SignUp} from '../pages/SignUp/SignUp';
import {SignIn} from '../pages/SignIn/SignIn';
import { Activate } from '../pages/Activate/Activate';

export const AppRoutes: FunctionComponent = () => {
    return (
        <Routes>
            <Route path={pathnames.account} element={<Home/>}/>
            <Route path={pathnames.signUp} element={<SignUp/>}/>
            <Route path={pathnames.signIn} element={<SignIn/>}/>
            <Route path={pathnames.activate} element={<Activate/>}/>
            <Route path="*" element={<NotFound/>}/>
        </Routes>
    );
};
