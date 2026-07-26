CREATE PROCEDURE usp_InsertOrder
    @symbol VARCHAR(10),
    @side VARCHAR(4),
    @quantity INT,
    @price DECIMAL(18,4),
    @status VARCHAR(20)
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO orders (symbol, side, quantity, price, status)
    VALUES (@symbol, @side, @quantity, @price, @status);

    SELECT CAST(SCOPE_IDENTITY() AS BIGINT) AS id;
END
