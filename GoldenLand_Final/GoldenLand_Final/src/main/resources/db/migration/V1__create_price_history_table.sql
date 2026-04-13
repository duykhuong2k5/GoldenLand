CREATE TABLE IF NOT EXISTS price_history (
	id BIGINT NOT NULL AUTO_INCREMENT,
	building_id BIGINT NOT NULL,
	period VARCHAR(6) NOT NULL,
	price_min DECIMAL(10,1),
	price_med DECIMAL(10,1),
	price_max DECIMAL(10,1),
	listings INT,
	createddate DATETIME NULL,
	createdby VARCHAR(255) NULL,
	modifieddate DATETIME NULL,
	modifiedby VARCHAR(255) NULL,
	PRIMARY KEY (id),
	CONSTRAINT uq_building_period UNIQUE (building_id, period),
	CONSTRAINT fk_price_history_building FOREIGN KEY (building_id) REFERENCES building(id)
);

CREATE INDEX idx_price_history_building ON price_history(building_id);
