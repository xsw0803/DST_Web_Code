# ICA3 Test Cases

## Test Objective

These test cases verify my individual contribution to the secondary development of the pharmacogenomics service website. The tested functions include the improved matching upload workflow, server-side validation, sample history navigation, knowledge base search, matching result dashboard, and client-side matched-result filtering.

## Test Environment

- IDE: IntelliJ IDEA
- Web server: Apache Tomcat 9
- Backend: Java Servlet, JSP, JDBC
- Database: MySQL
- Browser: Chrome
- Project branch: ICA
- Test dataset:
  - `ex1_new.hg19_multianno.csv`
  - `ex2_new.hg19_multianno.txt`
  - `test_match_CYP2D6.hg19_multianno.txt`

## Test Cases

| ID | Function | Test Input / Action | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|
| TC-01 | Matching upload page UI | Open the Matching page | The page displays `Patient Variant Matching`, workflow explanation, upload form, matching output description, and sample history button | The redesigned upload page was displayed correctly | Pass |
| TC-02 | Uploaded By validation | Submit the upload form without entering `Uploaded By` | The system rejects the submission and shows an error page | The validation error page displayed `Uploaded by can not be blank` | Pass |
| TC-03 | Empty file validation | Enter `Uploaded By`, but do not select an Annovar file | The system rejects the submission and shows an empty file error | The validation error page displayed `Annovar output file can not be blank` | Pass |
| TC-04 | Invalid file format validation | Upload `ex1_new.hg19_multianno.csv` with `Uploaded By` filled | The system rejects the comma-separated CSV file because the parser expects tab-delimited Annovar output | The validation error page displayed `annovar output file is invalid` | Pass |
| TC-05 | Valid Annovar upload | Upload `ex2_new.hg19_multianno.txt` with `Uploaded By` filled | The system saves the sample and redirects to the matching result page | A new sample record was created and the matching result page was displayed | Pass |
| TC-06 | Controlled matching test | Upload `test_match_CYP2D6.hg19_multianno.txt` | The system extracts `CYP2D6` and returns matched drug labels from the knowledge base | The matching result page displayed more than 100 matched drug labels | Pass |
| TC-07 | Matching result dashboard | Open a successful matching result page | The page displays sample metadata, uploaded user, uploaded time, and matched label count | Sample information and matched label count were displayed correctly | Pass |
| TC-08 | Matching result empty state | Open a sample with no matched labels | The page displays a clear empty result message | The page displayed `No matched drug labels were found for this sample` | Pass |
| TC-09 | Client-side matched-result filter | On a matching result page, type `fluoxetine` in the result filter | Only rows containing `fluoxetine` remain visible and the visible count updates | The result table was filtered without refreshing the page | Pass |
| TC-10 | Clear matched-result filter | Click the `Clear` button after filtering matching results | The filter input is cleared and all matched result rows become visible again | All rows were restored and the visible count returned to the original number | Pass |
| TC-11 | Sample history page | Open the Samples page | The page displays `Sample History`, a short description, an upload button, and sample records | The sample history page displayed the improved layout correctly | Pass |
| TC-12 | View previous matching result | Click `View Result` for a sample in the Samples page | The system opens the matching result page for the selected sample | The selected sample result page was opened correctly | Pass |
| TC-13 | Sample history empty state | Open Samples when no sample records are available | The page displays an empty-state message | The empty-state message was shown when no records were available | Pass |
| TC-14 | Drugs keyword search | Search `warfarin` on the Drugs page | The page displays drug records matching the keyword | Matching drug records were displayed | Pass |
| TC-15 | Drugs search empty state | Search `abcxyznotexist` on the Drugs page | The page displays no-result feedback | The page displayed `No drugs were found for the current search condition` | Pass |
| TC-16 | Drugs result count | Search on the Drugs page | The page shows the number of returned drug records | The result count changed according to the search result | Pass |
| TC-17 | Drug Labels keyword search | Search `CYP2D6` on the Drug Labels page | The page displays matching clinical label annotations | Matching drug label records were displayed | Pass |
| TC-18 | Drug Labels search empty state | Search `abcxyznotexist` on the Drug Labels page | The page displays no-result feedback | The page displayed `No drug labels were found for the current search condition` | Pass |
| TC-19 | Drug Labels result count | Search on the Drug Labels page | The page shows the number of returned drug label records | The result count changed according to the search result | Pass |
| TC-20 | Dosing Guidelines keyword search | Search `warfarin` or `CYP2C9` on the Dosing Guideline page | The page displays matching dosing guideline records | Matching dosing guideline records were displayed | Pass |
| TC-21 | Dosing Guidelines search empty state | Search `abcxyznotexist` on the Dosing Guideline page | The page displays no-result feedback | The page displayed `No dosing guidelines were found for the current search condition` | Pass |
| TC-22 | Dosing Guidelines result count | Search on the Dosing Guideline page | The page shows the number of returned dosing guideline records | The result count changed according to the search result | Pass |
| TC-23 | Homepage dashboard navigation | Open the Dashboard page and click each module card | Each card redirects to the corresponding system module | The dashboard cards linked to Matching, Samples, Drugs, Drug Labels, and Dosing Guidelines correctly | Pass |

## Summary of Test Results

The tests show that the enhanced matching workflow can validate user input, reject invalid files, accept valid Annovar-style tab-delimited files, create sample records, and display matching results. The sample history page allows users to reopen previous matching results. The three knowledge base pages support keyword search, reset, result count feedback, and empty-state messages. The matching result page supports client-side filtering, which helps users narrow down large result sets without another server request.

Overall, the implemented functions operated as expected during manual testing and were integrated with the existing Servlet, JSP, DAO, and MySQL-based architecture.

