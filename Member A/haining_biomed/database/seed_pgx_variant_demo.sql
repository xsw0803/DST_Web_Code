USE week21_db;

INSERT INTO pgx_variant(gene, variant_name, rs_id, function_status, phenotype, drug_id, evidence_source, interpretation)
VALUES ('CYP2C19', 'CYP2C19*2', 'rs4244285', 'decreased function', 'possible reduced metabolism', 'PA10075', 'CPIC/PharmGKB', 'This rsID is associated with altered CYP2C19 activity and may influence response to CYP2C19-related drugs.'),
       ('TPMT', 'TPMT decreased function variant', 'rs1142345', 'decreased function', 'possible intermediate/poor metabolizer', 'PA448515', 'CPIC/PharmGKB', 'This rsID has been reported in TPMT pharmacogenetic evidence and may increase thiopurine toxicity risk.'),
       ('NUDT15', 'NUDT15 decreased function variant', 'rs116855232', 'decreased function', 'possible increased thiopurine sensitivity', 'PA448515', 'CPIC/PharmGKB', 'This rsID is linked to NUDT15 reduced activity and may require thiopurine dose caution.'),
       ('CYP2D6', 'CYP2D6 reduced function variant', 'rs16947', 'decreased function', 'possible altered CYP2D6 metabolism', 'PA10026', 'CPIC/PharmGKB', 'This rsID may affect CYP2D6-mediated metabolism and influence dose/response for related drugs.'),
       ('VKORC1', 'VKORC1 sensitivity variant', 'rs7294', 'altered sensitivity', 'possible altered anticoagulant sensitivity', 'PA452632', 'CPIC/PharmGKB', 'This rsID has pharmacogenetic relevance for vitamin K antagonist sensitivity.'),
       ('DPYD', 'DPYD decreased function variant', 'rs3918290', 'decreased function', 'possible higher fluoropyrimidine toxicity risk', 'PA128406956', 'CPIC/PharmGKB', 'This rsID is associated with reduced DPYD activity and potential fluoropyrimidine toxicity risk.')
ON DUPLICATE KEY UPDATE
    variant_name = VALUES(variant_name),
    function_status = VALUES(function_status),
    phenotype = VALUES(phenotype),
    evidence_source = VALUES(evidence_source),
    interpretation = VALUES(interpretation);
