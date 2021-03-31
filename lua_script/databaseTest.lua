--author: Zhuoyi Li
-- lua script that testing if we can download information from the cloud database
-- through a GET request as well as if we can upload player's gaming score to database
-- through a PATCH request

url = "https://flappybird-7dea8-default-rtdb.firebaseio.com/User/test.json"
        
--get user data from Firebase database
print("Send a GET request to database:")
http.get(url, nil, function(code, data)
    if (code < 0) then
        print("HTTP request failed")
    else
        print(code, data)

        
        print("Update the user current score by a PATCH request to database:")

        --construct a json object we are going to send 
        ok, json = pcall(sjson.encode, {current_score="100"})
            if not ok then
                print("failed to encode!")
            end

        --update user's current score to Firebase database
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
