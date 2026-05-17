## Manual Testing Checklist

| Module | Test Case | Steps | Expected Result |
|---|---|---|---|
| Authentication | Register valid user | Open `Register`, enter a new username and matching passwords, then submit | Browser redirects to the login page with a successful registration message |
| Authentication | Register duplicate user | Try to register using an existing username | Registration page shows a duplicate username error |
| Authentication | Login valid user | Open `Login`, enter valid credentials, then submit | Browser redirects to the dashboard and left navigation shows the signed-in user |
| Authentication | Logout | Click `Logout` in the left navigation | Session is cleared and browser returns to the login page |
| Authentication | Protected route redirect | Access `Matching` or `Drugs` without login | Browser redirects to the login page |
| Drugs | Search existing keyword | Open `Drugs`, search `warfarin` | Related drug rows are displayed |
| Drugs | Search missing keyword | Open `Drugs`, search `zzzz_non_existing_drug_keyword` | Empty-state alert is displayed |
| Drugs | Reset search | Search a keyword, then click `Reset` | Full drug list is restored |
| Drugs | Result count | Search an existing and then a missing keyword | `Showing X result(s)` updates correctly |
| Drugs | Open detail page | Click a drug name from the list | Browser opens `Drug Detail` page with full record and related sections |
| Drugs | Detail page explanation | Click `Explain` on a drug detail page | Explanation block is displayed on the same page |
| Drugs | Save from detail page | Click `Save` on a drug detail page | Item appears on `Saved Items` page |
| Drug Labels | Search by source | Open `Drug Labels`, search `FDA` | Matching label rows are displayed |
| Drug Labels | Search by summary keyword | Open `Drug Labels`, search `CYP2D6` | Matching label rows are displayed |
| Drug Labels | Search missing keyword | Search `zzzz_non_existing_label_keyword` | Empty-state alert is displayed |
| Drug Labels | Reset search | Search a keyword, then click `Reset` | Full label list is restored |
| Drug Labels | Result count | Search existing and missing keywords | `Showing X result(s)` updates correctly |
| Drug Labels | Open detail page | Click a label name from the list | Browser opens `Drug Label Detail` page with summary, full text, and related sections |
| Drug Labels | Detail page explanation | Click `Explain` on a drug label detail page | Explanation block is displayed on the detail page |
| Drug Labels | Save from detail page | Click `Save` on a drug label detail page | Item appears on `Saved Items` page |
| Dosing Guidelines | Search by source | Open `Dosing Guideline`, search `CPIC` | Matching guideline rows are displayed |
| Dosing Guidelines | Search by drug/summary keyword | Search `fluoxetine` | Matching guideline rows are displayed |
| Dosing Guidelines | Search missing keyword | Search `zzzz_non_existing_guideline_keyword` | Empty-state alert is displayed |
| Dosing Guidelines | Reset search | Search a keyword, then click `Reset` | Full guideline list is restored |
| Dosing Guidelines | Result count | Search existing and missing keywords | `Showing X result(s)` updates correctly |
| Dosing Guidelines | Open detail page | Click a guideline name from the list | Browser opens `Dosing Guideline Detail` page with full record and related items |
| Dosing Guidelines | Detail page explanation | Click `Explain` on a dosing guideline detail page | Explanation block is displayed on the same page |
| Dosing Guidelines | Save from detail page | Click `Save` on a dosing guideline detail page | Item appears on `Saved Items` page |
| Matching Upload | Empty uploaded by | Open `Matching`, leave `Uploaded By` blank, submit | Validation error page shows `Uploaded by can not be blank` |
| Matching Upload | Empty file | Fill `Uploaded By`, do not choose a file, submit | Validation error page shows `Annovar output file can not be blank` |
| Matching Upload | Invalid file format | Upload invalid `.csv` Annovar-like file | Validation error page shows `annovar output file is invalid` |
| Matching Upload | Valid file upload | Upload valid tab-delimited Annovar file | System redirects to the matching result page |
| Matching Result | Sample info card | Open a matching result page | Sample id, uploaded time, and uploaded by are visible |
| Matching Result | Result count card | Open a matching result page with matches | Matched Drug Labels count is displayed |
| Matching Result | Result filter | Type `fluoxetine` into the filter box | Visible rows are reduced and count updates |
| Matching Result | Clear filter | Click `Clear` after filtering | All matched rows are restored |
| Matching Result | Empty filter result | Type a keyword that matches no row | No rows remain visible and visible count becomes `0` |
| Matching Result | Sample explanation | Click `Explain This Sample` | Sample explanation block is displayed using AI or fallback explanation |
| Matching Result | Label-level explanation | Click `Explain` in a matched row | Browser opens the related drug label detail page with explanation content |
| Matching Result | Save matched label | Click `Save` in a matched row | Button state changes or item appears on `Saved Items` page |
| Matching Result | CSV export | Click `Export CSV` | CSV file downloads successfully |
| Matching Result | Export readability | Open downloaded CSV | CSV contains `sample_id, drug_label_id, label_name, source, summary` and readable `label_name` values |
| Matching Result | PDF-style export | Click `Export PDF` | Browser opens the print-friendly report page and the print dialog appears |
| Sample History | Navigation | Open `Samples` page | Sample list and `View Result` buttons are visible |
| Sample History | Reopen result | Click `View Result` on an existing sample with stored result data | Browser opens the matching result page for that sample |
| Saved Items | Saved item listing | Open `Saved Items` after saving multiple records | Saved items list shows saved record type, identifier, and navigation links |
| Saved Items | Remove saved item | Click `Remove` for a saved item | Item disappears from the saved items list |
| Dashboard | Navigation cards | Open the homepage and click module cards | Each card navigates to the correct module page |
