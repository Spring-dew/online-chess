import {AbstractControl, ValidationErrors, ValidatorFn} from '@angular/forms';

export const retypePasswordValidator: ValidatorFn = (
  control: AbstractControl,
): ValidationErrors | null => {
  const password = control.get('password');
  const passwordConfirmation = control.get('passwordConfirmation');

  return password && passwordConfirmation && password.value?.length > 0 && passwordConfirmation.value?.length > 0 && password.value !== passwordConfirmation.value
    ? {passwordMismatch: true}
    : null;
};
