BEGIN TRANSACTION;
CREATE OR REPLACE FUNCTION convert_bytes_to_human_readable(size_in_bytes bigint)
    RETURNS VARCHAR AS
$$
DECLARE
    units CONSTANT TEXT[]           := ARRAY ['KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    size           DOUBLE PRECISION := size_in_bytes;
    unit_index     INTEGER          := 0;
BEGIN
    WHILE size >= 1024 AND unit_index < array_length(units, 1) - 1
        LOOP
            size := size / 1024;
            unit_index := unit_index + 1;
        END LOOP;

    RETURN CONCAT(size, ' ', units[unit_index]);
END
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION format_currency(
    value FLOAT,
    decimalDigits INTEGER = 2
)
    RETURNS VARCHAR AS
$$
DECLARE
    pattern VARCHAR := 'FM$999,999,999,999,999,999,990.' ||
                       CASE
                           WHEN decimalDigits >= 2 THEN '00' || RPAD('9', decimalDigits - 2, '9')
                           WHEN decimalDigits = 1 THEN '0'
                           ELSE ''
                           END;
BEGIN
    RETURN CASE
               WHEN value >= 0 THEN TO_CHAR(value, pattern)
               ELSE '-'
        END;
END;
$$ LANGUAGE plpgsql;


DROP VIEW IF EXISTS plans_billings;
CREATE VIEW plans_billings AS
SELECT name,
       period,
       max_files,
       max_storage                                  as max_storage_bytes,
       convert_bytes_to_human_readable(max_storage) as max_storage,
       format_currency(price)                       as price,
       format_currency(real_price, 10)              as real_price,
       format_currency(price - real_price, 10)      as profit,
       (price - real_price) / price * 100           as profit_percent,
       storage_discount,
       period_discount,
       storage_discount + period_discount           as total_discount,
       backup,
       short_url,
       billings.id                                  as billing_id,
       p.id                                         as plan_id,
       available
FROM billings
         INNER JOIN public.plans p on p.id = billings.plan_id;
END;
