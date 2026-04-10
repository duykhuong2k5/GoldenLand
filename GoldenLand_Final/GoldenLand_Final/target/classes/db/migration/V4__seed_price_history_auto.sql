-- V4__seed_price_history_auto.sql
-- Seed 4 quý gần nhất cho mọi building (không dùng CTE để tránh parser của Flyway)

INSERT INTO price_history (
    building_id, period, price_min, price_med, price_max, listings,
    createddate, createdby
)
SELECT
    b.id,
    CONCAT(
        YEAR(DATE_SUB(CURDATE(), INTERVAL q.n QUARTER)),
        'Q',
        QUARTER(DATE_SUB(CURDATE(), INTERVAL q.n QUARTER))
    ) AS period,

    -- ===== min =====
    ROUND(
        (
            (40 + (b.id % 25))                                       -- nền theo ID tòa
            + ((3 - q.n) * 0.4)                                      -- xu hướng theo thời gian (4 quý)
            + ((CONV(SUBSTRING(SHA1(CONCAT('med', b.id,
                 CONCAT(YEAR(DATE_SUB(CURDATE(), INTERVAL q.n QUARTER)), 'Q',
                        QUARTER(DATE_SUB(CURDATE(), INTERVAL q.n QUARTER)))
            )),1,8),16,10) % 20) / 10.0)                             -- nhiễu 0..1.9
        )
        - (0.05 * (
            (40 + (b.id % 25)) + ((3 - q.n) * 0.4)
            + ((CONV(SUBSTRING(SHA1(CONCAT('med', b.id,
                 CONCAT(YEAR(DATE_SUB(CURDATE(), INTERVAL q.n QUARTER)), 'Q',
                        QUARTER(DATE_SUB(CURDATE(), INTERVAL q.n QUARTER)))
            )),1,8),16,10) % 20) / 10.0)
        ))
        - ((CONV(SUBSTRING(SHA1(CONCAT('min', b.id,
             CONCAT(YEAR(DATE_SUB(CURDATE(), INTERVAL q.n QUARTER)), 'Q',
                    QUARTER(DATE_SUB(CURDATE(), INTERVAL q.n QUARTER)))
        )),1,8),16,10) % 5) / 10.0),
        1
    ) AS price_min,

    -- ===== med (giá phổ biến) =====
    ROUND(
        (40 + (b.id % 25))
        + ((3 - q.n) * 0.4)
        + ((CONV(SUBSTRING(SHA1(CONCAT('med', b.id,
             CONCAT(YEAR(DATE_SUB(CURDATE(), INTERVAL q.n QUARTER)), 'Q',
                    QUARTER(DATE_SUB(CURDATE(), INTERVAL q.n QUARTER)))
        )),1,8),16,10) % 20) / 10.0),
        1
    ) AS price_med,

    -- ===== max =====
    ROUND(
        (
            (40 + (b.id % 25))
            + ((3 - q.n) * 0.4)
            + ((CONV(SUBSTRING(SHA1(CONCAT('med', b.id,
                 CONCAT(YEAR(DATE_SUB(CURDATE(), INTERVAL q.n QUARTER)), 'Q',
                        QUARTER(DATE_SUB(CURDATE(), INTERVAL q.n QUARTER)))
            )),1,8),16,10) % 20) / 10.0)
        )
        + 0.08 * (
            (40 + (b.id % 25)) + ((3 - q.n) * 0.4)
            + ((CONV(SUBSTRING(SHA1(CONCAT('med', b.id,
                 CONCAT(YEAR(DATE_SUB(CURDATE(), INTERVAL q.n QUARTER)), 'Q',
                        QUARTER(DATE_SUB(CURDATE(), INTERVAL q.n QUARTER)))
            )),1,8),16,10) % 20) / 10.0)
        )
        + ((CONV(SUBSTRING(SHA1(CONCAT('max', b.id,
             CONCAT(YEAR(DATE_SUB(CURDATE(), INTERVAL q.n QUARTER)), 'Q',
                    QUARTER(DATE_SUB(CURDATE(), INTERVAL q.n QUARTER)))
        )),1,8),16,10) % 5) / 10.0),
        1
    ) AS price_max,

    -- ===== listings =====
    5 + (CONV(SUBSTRING(SHA1(CONCAT('list', b.id,
         CONCAT(YEAR(DATE_SUB(CURDATE(), INTERVAL q.n QUARTER)), 'Q',
                QUARTER(DATE_SUB(CURDATE(), INTERVAL q.n QUARTER)))
    )),1,8),16,10) % 11) AS listings,

    NOW() AS createddate,
    'seed' AS createdby
FROM building b
CROSS JOIN (
    SELECT 0 AS n UNION ALL
    SELECT 1 UNION ALL
    SELECT 2 UNION ALL
    SELECT 3
) AS q
LEFT JOIN price_history ph
  ON ph.building_id = b.id
 AND ph.period = CONCAT(
        YEAR(DATE_SUB(CURDATE(), INTERVAL q.n QUARTER)),
        'Q',
        QUARTER(DATE_SUB(CURDATE(), INTERVAL q.n QUARTER))
     )
WHERE ph.id IS NULL;
