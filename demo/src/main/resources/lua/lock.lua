-- 1 代表 true
-- 0 代表 false
-- KEYS:[lock],ARGV[1000,uuid]
--加锁代码首先使用 Redis exists 命令判断当前 lock 这个锁是否存在。
--如果锁不存在的话，直接使用 hincrby创建一个键为 lock hash 表，并且为 Hash 表中键为 uuid 初始化为 0，然后再次加 1，最后再设置过期时间。
--如果当前锁存在，则使用 hexists判断当前 lock 对应的 hash 表中是否存在 uuid 这个键，如果存在,再次使用 hincrby 加 1，最后再次设置过期时间。
--最后如果上述两个逻辑都不符合，直接返回。

if (redis.call('exists', KEYS[1]) == 0) then
    redis.call('hincrby', KEYS[1], ARGV[2], 1);
    redis.call('pexpire', KEYS[1], ARGV[1]);
    return 1;
end ;
if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then
    redis.call('hincrby', KEYS[1], ARGV[2], 1);
    redis.call('pexpire', KEYS[1], ARGV[1]);
    return 1;
end ;
return 0;