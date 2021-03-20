
url = "https://flappybird-7dea8-default-rtdb.firebaseio.com/User/test.json"
        
--get user data from Firebase database
print("Send a GET request to database:")
http.get(url, nil, function(code, data)
    if (code < 0) then
        print("HTTP request failed")
    else
        print(code, data)

        --update user's current score to Firebase database
        print("Update the user current score by a PATCH request to database:")
        ok, json = pcall(sjson.encode, {current_score="100"})
            if not ok then
                print("failed to encode!")
            end


        http.request(url, "PATCH", "", json,
        function(code, data)
            if (code < 0) then
            print("HTTP request failed")
            else
            print(code, data)
            end
        end) 
    end
  end)

--
--url = "https://flappybird-7dea8-default-rtdb.firebaseio.com/User/AAA.json"
--
--http.request(url, "PATCH", "", json,
--  function(code, data)
--    if (code < 0) then
--      print("HTTP request failed")
--    else
--      print(code, data)
--    end
--  end)
--
