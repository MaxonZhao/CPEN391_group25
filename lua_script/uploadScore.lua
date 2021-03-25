 --
--helper function for uploadScore()
--
function buildURL(user)
    url = "https://flappybird-7dea8-default-rtdb.firebaseio.com/User/"..user..".json"
    return url
end

--
--helper function for uploadScore()
--
function covert2Json(data)
    ok, json = pcall(sjson.encode, data)
    if not ok then
        print("failed to encode!")
    end
    return json
end

--
--update user's current score to the cloud Firebase database 
--through a PATCH request
--  user(string): usr name we want to update his/her current_score
--  score(int): game to update
--
function uploadScore(user,score)
--   print("Update the user current score by a PATCH request to database:")

    data = {
        current_score = score
    }
    
    json = covert2Json(data)
    url = buildURL(user)
   
    http.request(url, "PATCH", "", json,
        function(code, data)
        if (code < 0) then
          print("HTTP request failed")
        else
          print(code, data)
        end
   end)
end

