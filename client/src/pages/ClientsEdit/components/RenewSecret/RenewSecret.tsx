import { FunctionComponent, MouseEvent, useState } from 'react';
import { Captions } from '../../../../constants';

import './RenewSecret.css';

export const RenewSecret: FunctionComponent = () => {
  const [newSecret, setNewSecret] = useState('');

  const handleRenew = (event: MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
    setNewSecret(Math.random().toString().split('.')[1]);
  };

  return (
    <div className='renew-secret'>
      {!!newSecret && (
        <div>{Captions.NewSecret}: {newSecret}</div>
      )}
      <button onClick={handleRenew}>{Captions.RenewSecret}</button>
    </div>
  );
};
