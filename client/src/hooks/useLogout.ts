import { accountsApiDeclaration } from '../apiDeclarations';
import { Profile } from '../pages/Account/Account';
import { useApiMethod } from './useApiMethod';

export const useLogout = () => {
  const {
    apiMethodState: logoutMethodState,
    fetchData: logoutFetch
  } = useApiMethod<Profile, undefined>(accountsApiDeclaration.logout);

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