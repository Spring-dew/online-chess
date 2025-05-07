import {AbstractControl, ValidationErrors, ValidatorFn} from '@angular/forms';

export function invalidPasswordValidator(password: RegExp) : ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const invalid = password.test(control.value);
    return invalid ? { invalidName: { value: control.value } } : null;
  };
}
