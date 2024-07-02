import { FunctionComponent, MouseEvent } from 'react';
import { Captions } from '../../../../constants';
import { RefreshAppParams, appsApiDeclaration } from '../../../../apiDeclarations';
import { useApiMethod } from '../../../../hooks/useApiMethod';
import { Loader } from '../../../../components/Loader/Loader';

import './RenewSecret.css';

export const RenewSecret: FunctionComponent<RefreshAppParams> = ({
  clientId,
}) => {
  const {
    apiMethodState,
    fetchData,
  } = useApiMethod<string, RefreshAppParams>(appsApiDeclaration.refresh);

  const {
    data,
    process: { loading, error },
  } = apiMethodState;

  const handleRenew = (event: MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
    fetchData({
      clientId,
    });
  };

  return (
    <div className='renew-secret'>
      {loading && <Loader />}
      {!!error && <div>{Captions.Error}: {error}</div>}
      {!!data && (
        <div>{Captions.NewSecret}: {data}</div>
      )}
      <button onClick={handleRenew}>{Captions.RenewSecret}</button>
    </div>
  );
};
