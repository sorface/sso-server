import {FunctionComponent} from 'react';
import {Route, Routes} from 'react-router-dom';
import {pathnames} from '../constants';
import {Account} from '../pages/Account/Account';
import {NotFound} from '../pages/NotFound/NotFound';
import {SignIn} from '../pages/SignIn/SignIn';
import {Activate} from '../pages/Activate/Activate';
import {Session} from "../pages/Session/Session";

export const AppRoutes: FunctionComponent = () => {
    return (
        <Routes>
            <Route path={pathnames.account} element={<Account/>}/>
            <Route path={pathnames.session} element={<Session/>}/>
            {/*<Route path={pathnames.signUp} element={<SignUp/>}/>*/}
            <Route path={pathnames.signIn} element={<SignIn/>}/>
            <Route path={pathnames.activate} element={<Activate/>}/>
            <Route path="*" element={<NotFound/>}/>
        </Routes>
    );
};
