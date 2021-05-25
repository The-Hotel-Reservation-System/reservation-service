CREATE TABLE reservation
(
    id bigserial PRIMARY KEY,
    hotel_Id bigserial NOT NULL,
    guest_Id bigserial NOT NULL,
    room_Type VARCHAR(64) NOT NULL,
    room_Id bigserial NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE,
    updated_date TIMESTAMP WITH TIME ZONE,
    start_date TIMESTAMP WITH TIME ZONE,
    end_date TIMESTAMP WITH TIME ZONE,
    total VARCHAR(255),
    status VARCHAR(64)
);