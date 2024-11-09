
python_cmd="python"
if [[ ! -x "$(command -v "${python_cmd}")" ]]
then
  python_cmd="python3"
fi

printf "\n" "python_cmd=${python_cmd}"

venv_dir="venv"
if [[ ! -d "${venv_dir}" ]]
then
    printf "\n" "Create and activate python venv"
    "${python_cmd}" -m venv "${venv_dir}"
    "${venv_dir}"/bin/python -m pip install --upgrade pip
fi

if [[ -f "${venv_dir}"/bin/activate ]]
then
    source "${venv_dir}"/bin/activate
    # ensure use of python from venv
    python_cmd="${venv_dir}"/bin/python
else
    printf "\n" "ERROR: Cannot activate python venv, aborting..."
    exit 1
fi

pip install -r requirements.txt

printf "\033[32mInstall successful\033[0m"
printf "\n"

cp *.apk "apk.zip"
unzip -d tmp/ apk.zip

"${python_cmd}" extractResources.py

rm "apk.zip"
rm -rf tmp