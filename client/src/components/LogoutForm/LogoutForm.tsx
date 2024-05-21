import {FunctionComponent} from 'react';
import {Form, FormProps} from '../Form/Form';
import {Captions} from '../../constants';

import './LogoutForm.css';
import {REACT_APP_BACKEND_URL} from "../../config";

export interface LogoutFormProps {
  submitCaption?: FormProps['submitCaption'];
  children?: FormProps['children'];
}

export const LogoutForm: FunctionComponent<LogoutFormProps> = ({
  submitCaption,
  children,
}) => {
  return (
    <Form
      htmlMethod='POST'
      htmlAction={REACT_APP_BACKEND_URL + '/api/accounts/logout'}
      className='form-logout'
      fields={[]}
      fieldErrors={{ '': '' }}
      submitCaption={submitCaption ?? Captions.Logout}
      children={children}
    />
  );
};
