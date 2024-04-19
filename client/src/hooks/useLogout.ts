import { useEffect } from 'react';
import { accountsApiDeclaration } from '../apiDeclarations';
import { useApiMethod } from './useApiMethod';
import { Account } from '../types/account';
import { pathnames } from '../constants';

export const useLogout = () => {
  const {
    apiMethodState: logoutMethodState,
    fetchData: logoutFetch
  } = useApiMethod<Account, undefined>(accountsApiDeclaration.logout);

  const {
    process: {
      loading: logoutLoading,
      error: logoutError
    },
    data: logoutData
  } = logoutMethodState;

  useEffect(() => {
    if (!logoutData) {
        return;
    }

    window.location.assign(pathnames.signIn)
}, [logoutData]);

  return {
    logoutLoading,
    logoutError,
    logoutFetch,
  };
};