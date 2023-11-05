import { FunctionComponent } from 'react';
import { Routes, Route } from 'react-router-dom';
import { pathnames } from '../constants';
import { Home } from '../pages/Home/Home';
import { NotFound } from '../pages/NotFound/NotFound';
import { SignIn } from '../pages/SignIn/SignIn';
import { LogIn } from '../pages/LogIn/Login';

export const AppRoutes: FunctionComponent = () => {
  return (
    <Routes>
      <Route path={pathnames.home} element={<Home />} />
      <Route path={pathnames.signIn} element={<SignIn />} />
      <Route path={pathnames.logIn} element={<LogIn />} />
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
};
