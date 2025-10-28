ALTER TABLE price_history
  ADD COLUMN createdby    VARCHAR(255)     NULL,
  ADD COLUMN createddate  DATETIME         NULL,
  ADD COLUMN modifiedby   VARCHAR(255)     NULL,
  ADD COLUMN modifieddate DATETIME         NULL;
