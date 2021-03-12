#!/bin/bash
# Copyright 2021 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

set -e

TMP_REPORT_DIR=$(mktemp -d)
SUCCEEDED_FILE=${TMP_REPORT_DIR}/succeeded
FAILED_FILE=${TMP_REPORT_DIR}/failed

# Set flags for skipping tests if we are not authenticated
if [ ! -s "$GOOGLE_APPLICATION_CREDENTIALS" ]; then
    MAVEN_FLAGS="-Dmaven.test.skip=true"
    GRADLE_FLAGS="-x test"
    BUILD_ONLY="(build only) "
fi

build_samples()
{
    if [ "$2" == "maven" ]; then
        BUILD_COMMAND="./mvnw install ${MAVEN_FLAGS}"
    else
        BUILD_COMMAND="./gradlew build ${GRADLE_FLAGS}"
    fi
    echo $BUILD_COMMAND
    # Temporarily allow error
    set +e
    $BUILD_COMMAND < /dev/null
    if [ $? == 0 ]; then
        echo "$1 ($2): ok" >> "${SUCCEEDED_FILE}"
    else
        if [[ " ${FLAKES[@]} " =~ " ${DIR} " ]]; then
            echo "$1 ($2): failed" >> "${FAILED_FLAKY_FILE}"
        else
            echo "$1 ($2): failed" >> "${FAILED_FILE}"
        fi
    fi
    set -e
}

# Loop through all directories containing "gradlew" and run the test suites.
find * \( -name 'gradlew' -o -name 'mvnw' \) -exec dirname {} \; | while read DIR
do
    pushd ${DIR}
    if [ -f "gradlew" ]; then
        build_samples $DIR "gradle"
    fi
    if [ -f "mvnw" ]; then
        build_samples $DIR "maven"
    fi
    popd
done

set +x
if [ -f "${SUCCEEDED_FILE}" ]; then
    echo "--------- Succeeded ${BUILD_ONLY}-----------"
    cat "${SUCCEEDED_FILE}"
    echo "-------------------------------------"
fi

if [ -f "${FAILED_FILE}" ]; then
    echo "--------- Failed ${BUILD_ONLY}--------------"
    cat "${FAILED_FILE}"
    echo "-------------------------------------"
fi

# Finally report failure if any buildsfailed
if [ -f "${FAILED_FILE}" ]; then
    exit 1
fi
