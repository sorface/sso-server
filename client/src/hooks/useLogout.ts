import { accountsApiDeclaration } from '../apiDeclarations';
import { useApiMethod } from './useApiMethod';
import { Account } from '../types/account';

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

  return {
    logoutLoading,
    logoutError,
    logoutData,
    logoutFetch,
  };
};